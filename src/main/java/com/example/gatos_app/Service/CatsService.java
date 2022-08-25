package com.example.gatos_app.Service;

import com.example.gatos_app.Model.Cats;
import com.example.gatos_app.Model.FavoriteCats;
import com.google.gson.Gson;
import okhttp3.*;

import javax.imageio.ImageIO;
import javax.net.ssl.HttpsURLConnection;
import javax.swing.*;
import java.awt.*;
import java.io.IOException;
import java.net.URL;
import java.util.Objects;

public class CatsService {

    private static final String BASE_URL = "https://api.thecatapi.com/v1/";
    private static final String SEARCH_ENDPOINT = BASE_URL + "images/search";
    private static final String FAVORITE_ENDPOINT = BASE_URL + "favourites/";

    private static final String randomCatsMenu = "Opciones: \n"
            + " 1. ver otra imagen \n"
            + " 2. Favorito \n"
            + " 3. Volver \n";

    private static final String FavoriteMenu = "Opciones: \n"
            + " 1. ver otra imagen \n"
            + " 2. Eliminar Favorito \n"
            + " 3. Volver \n";

    public static void seeCats() throws IOException {
        // 1. vamos a traer los datos de la api
        OkHttpClient client = new OkHttpClient().newBuilder().build();
        Request request = new Request.Builder().url(SEARCH_ENDPOINT)
                .method("GET", null)
                .build();
        Response response = client.newCall(request).execute();

        String theJson = Objects.requireNonNull(response.body()).string();

        // Vamos a cortar los corchectes del body del json
        theJson = theJson.substring(1, theJson.length());
        theJson = theJson.substring(0, theJson.length()-1);

        // Crear un objeto de la clase Gson
        Gson gson = new Gson();

        //Convertir la respuesta de la API  a un objeto de tipo Cats
        Cats cats = gson.fromJson(theJson, Cats.class);

        //Redimensionar una imagen en caso de que sea necesario
        Image image;
        try{
            URL url = new URL(cats.getUrl());

            //HttpsURLConnection es otra clase que se utiliza para el protocolo HTTPS más seguro.
            HttpsURLConnection httpconnection = (HttpsURLConnection) url.openConnection();
            httpconnection.addRequestProperty("User-Agent", "");

            image = ImageIO.read(httpconnection.getInputStream());

            ImageIcon catBackground = new ImageIcon(image);

            if(catBackground.getIconWidth() > 800){
                //Renderizamos la imagen
                Image background = catBackground.getImage();
                Image modified = background.getScaledInstance(800, 600, Image.SCALE_SMOOTH);
                catBackground = new ImageIcon(modified);
            }

            String[] buttons = { "ver otra imagen", "favorito", "volver" };
            String catId = cats.getId();
            String option = (String) JOptionPane.showInputDialog(null, randomCatsMenu, catId, JOptionPane.INFORMATION_MESSAGE, catBackground, buttons,buttons[0]);

            int selection = -1;

            //bucle para recorrer la coleccion buttons y capturar la selección del usuario
            for(int i = 0; i < buttons.length; i++){
                if(option.equals(buttons[i])){
                    selection = i;
                }
            }

            switch(selection){
                case 0:
                    seeCats();
                    break;
                case 1:
                    markCatAsFavorite(cats);
                    break;
                default:
                    break;
            }

        }catch(IOException e){
            System.out.println(e);
        }
    }

    public static void markCatAsFavorite(Cats cats){
        try{
            OkHttpClient client = new OkHttpClient().newBuilder().build();
            MediaType mediaType = MediaType.parse("application/json");
            RequestBody body = RequestBody.create(mediaType, "{\r\n\t\"image_id\":\""+cats.getId()+"\"\r\n}");
            Request request = new Request.Builder()
                    .url(FAVORITE_ENDPOINT)
                    .method("POST", body)
                    .addHeader("Content-Type", "application/json")
                    .addHeader("x-api-key", cats.getApikey())
                    .build();
            Response response = client.newCall(request).execute();
        }catch(IOException e){
            System.out.println(e);
        }
    }

    public static void seeFavoriteCats(String apikey) throws IOException {
        OkHttpClient client = new OkHttpClient().newBuilder().build();
        Request request = new Request.Builder()
                .url(FAVORITE_ENDPOINT)
                .method("GET", null)
                .addHeader("x-api-key", apikey)
                .build();
        Response response = client.newCall(request).execute();

        // Guardamos el string con la respuesta
        String theJson = Objects.requireNonNull(response.body()).string();

        // Crear un objeto de la clase Gson
        Gson gson = new Gson();

        FavoriteCats[] favorites_cats = gson.fromJson(theJson, FavoriteCats[].class);

        if(favorites_cats.length > 0){
            int min = 1;
            int max  = favorites_cats.length;
            int aleatorio = (int) (Math.random() * ((max-min)+1)) + min;
            int indice = aleatorio-1;

            FavoriteCats favoriteCat = favorites_cats[indice];

            //Redimensionar una imagen en caso de que sea necesario
            Image image = null;
            try{
                URL url = new URL(favoriteCat.image.getUrl());

                //HttpsURLConnection es otra clase que se utiliza para el protocolo HTTPS más seguro.
                HttpsURLConnection httpconnection = (HttpsURLConnection) url.openConnection();
                httpconnection.addRequestProperty("User-Agent", "");

                image = ImageIO.read(httpconnection.getInputStream());

                ImageIcon catBackground = new ImageIcon(image);

                if(catBackground.getIconWidth() > 800){
                    //Renderizamos la imagen
                    Image background = catBackground.getImage();
                    Image modified = background.getScaledInstance(800, 600, Image.SCALE_SMOOTH);
                    catBackground = new ImageIcon(modified);
                }

                String[] buttons = { "ver otra imagen", "Eliminar favorito", "volver" };
                String catId = favoriteCat.getId();
                String option = (String) JOptionPane.showInputDialog(null, FavoriteMenu, catId, JOptionPane.INFORMATION_MESSAGE, catBackground, buttons,buttons[0]);

                int selection = -1;

                //bucle para recorrer la coleccion buttons y capturar la selección del usuario
                for(int i = 0; i < buttons.length; i++){
                    if(option.equals(buttons[i])){
                        selection = i;
                    }
                }

                switch(selection){
                    case 0:
                        seeFavoriteCats(apikey);
                        break;
                    case 1:
                       deleteFavoriteCat(favoriteCat);
                        break;
                    default:
                        break;
                }
        }catch(IOException e){
                System.out.println(e);
            }
    }
}

    public static void  deleteFavoriteCat(FavoriteCats favoriteCat){
        try{
            OkHttpClient client = new OkHttpClient().newBuilder()
                    .build();
            MediaType mediaType = MediaType.parse("application/json");
            RequestBody body = RequestBody.create(mediaType, "");
            Request request = new Request.Builder()
                    .url(FAVORITE_ENDPOINT+favoriteCat.getId()+"")
                    .method("DELETE", body)
                    .addHeader("Content-Type", "application/json")
                    .addHeader("x-api-key", favoriteCat.getApikey())
                    .build();
            Response response = client.newCall(request).execute();

            //Confirmar la respuesta del llamado de la api usando la instancia response y el metodo code()
            if(response.code() == 200) {
                JOptionPane.showMessageDialog(null, "Gato Favorito " + favoriteCat.getId() + " Eliminado ");
            }else {
                JOptionPane.showMessageDialog(null, "Algo a fallado " + response.code());
            }
        }catch(IOException e){
            System.out.println(e);
        }
    }
}

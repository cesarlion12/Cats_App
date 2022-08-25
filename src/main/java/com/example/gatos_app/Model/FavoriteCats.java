package com.example.gatos_app.Model;
import io.github.cdimascio.dotenv.Dotenv;


public class FavoriteCats {

    Dotenv dotenv = Dotenv.load();
    public Cats image;
    String id;
    String image_id;
    String apikey = dotenv.get("API_KEY");
    ImageX imageX;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getImage_id() {
        return image_id;
    }

    public void setImage_id(String image_id) {
        this.image_id = image_id;
    }

    public String getApikey() {
        return apikey;
    }

    public void setApikey(String apikey) {
        this.apikey = apikey;
    }

    public ImageX getImageX() {
        return imageX;
    }

    public void setImageX(ImageX imageX) {
        this.imageX = imageX;
    }
}

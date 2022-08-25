package com.example.gatos_app;

import com.example.gatos_app.Model.Cats;
import com.example.gatos_app.Service.CatsService;

import javax.swing.*;
import java.io.IOException;
import java.util.ArrayList;

public class Inicio {

    public static void main(String[] args) throws IOException {
        int selectedOption = -1;
        ArrayList<String> option = new ArrayList<>();
        option.add("1. Ver gatitos");
        option.add("2. Ver favoritos");
        option.add("3. Salir");

        do {
            Object input = JOptionPane.showInputDialog(null,"Gatitos Java","Menu Principal",JOptionPane.INFORMATION_MESSAGE,null,option.toArray(),option.get(0));

            selectedOption = option.indexOf(input);

            if(selectedOption == 0){

            }
            switch (selectedOption){
                case 0 :
                    CatsService.seeCats();
                    break;
                case 1 :
                    Cats cats = new Cats();
                    CatsService.seeFavoriteCats(cats.getApikey());
                    break;
            }
        }while(selectedOption != 1);
    }
}

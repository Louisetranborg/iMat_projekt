package sample;

import javafx.event.Event;
import javafx.fxml.FXML;

import java.io.IOException;

public class SettingsItem extends MenuItem {

    SettingsCategory settings;

    public enum SettingsCategory {
        HISTORY,
        FAVORITES,
        PERSONAL_DATA,
    }

    public SettingsItem(SearchController parentController, SettingsCategory settings) {
        super(parentController);

        this.settings = settings;
        setNames(settings);
    }

    private void setNames(SettingsCategory settings) {
        switch (settings) {
            case FAVORITES:
                categoryButton.setText("Favoriter");
                break;
            case PERSONAL_DATA:
                categoryButton.setText("Personuppgifter");
                break;
            case HISTORY:
                categoryButton.setText("Historik");
                break;
        }
    }


    public void onClickHistory() {
        parentController.productScrollPane.setContent(parentController.historyPage);
    }


    public void onClickFavorites() {
        parentController.productScrollPane.setContent(parentController.favoritePage);
        parentController.updateFavoritePage();
    }


    public void onClickPersonalAccount() {
        parentController.updatePersonalDataPage();
        parentController.productScrollPane.setContent(parentController.personalDataPage);
    }

    @FXML
    protected void onClick(Event event) throws IOException {        //När man klickar på en kategori skall varorna i mitten uppdateras

        System.out.println(settings);
        switch (settings) {
            case HISTORY:
                onClickHistory();
                break;
            case PERSONAL_DATA:
                onClickPersonalAccount();
                break;
            case FAVORITES:
                onClickFavorites();
                break;
        }
        parentController.changeCategoryPageText(categoryButton.getText());
        parentController.productScrollPane.setVvalue(0);
    }
}

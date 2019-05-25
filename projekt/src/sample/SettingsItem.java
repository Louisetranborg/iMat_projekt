package sample;

import javafx.event.Event;
import javafx.fxml.FXML;

import java.io.IOException;

public class SettingsItem extends MenuItem {

    SettingsCategory settings;

    public enum SettingsCategory {
        HISTORY,
        FAVORITES,
        SHOPPING_LIST,
        PERSONAL_DATA,
        BACK
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
            case SHOPPING_LIST:
                categoryButton.setText("Sparade kundvagnar");
                break;
            case PERSONAL_DATA:
                categoryButton.setText("Personuppgifter");
                break;
            case HISTORY:
                categoryButton.setText("Historik");
                break;
            case BACK:
                categoryButton.setText("Tillbaka till sortimentet");
                break;
        }
    }


    public void onClickMyPages() {

        //myPage.prefWidth(Region.USE_COMPUTED_SIZE);

        parentController.myPage.settingsPane.toFront();

    }


    public void onClickHistory() {
        parentController.myPage.historyPage.toFront();
    }


    public void onClickSavedCarts() {
        parentController.myPage.savedCartPage.toFront();
    }


    public void onClickPersonalAccount() {
        parentController.myPage.personalDataPage.toFront();
    }

    private void onClickBack(){
        parentController.updateFrontPage();
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
            case SHOPPING_LIST:
                onClickSavedCarts();
                break;
            case FAVORITES:
                onClickMyPages();
                break;
            case BACK:
                onClickBack();
                break;

        }

        // if(settings != SettingsCategory.BACK)
        // parentController.updateSettingsPaneFromSettings();
        //System.out.println(parentController.settingsScrollPane.getContent());


    }
}

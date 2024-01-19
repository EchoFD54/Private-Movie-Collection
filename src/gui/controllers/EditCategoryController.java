package gui.controllers;

import be.Category;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

public class EditCategoryController {

    public TextField editCategoryName;
    public MainWindowController mainWindowController;
    public Category selectedCategory;


    public void setMainWindowController(MainWindowController controller){
        this.mainWindowController = controller;
    }

    public void setSelectedCategoryName(Category categoryName){
        this.selectedCategory = categoryName;
        editCategoryName.setText(categoryName.getName().get());
    }

    public void saveEditCategoryButton(){
        if (selectedCategory != null) {
            String newCategory = editCategoryName.getText().trim();

            if (!newCategory.isEmpty()) {
                mainWindowController.editCategory(newCategory, selectedCategory);
                closeEditedWindow();
            }
        }
    }

    public void closeEditedWindow(){
        Stage stage = (Stage) editCategoryName.getScene().getWindow();
        stage.close();
    }

    public void cancelEditCategoryButton(){
        closeEditedWindow();
    }
}

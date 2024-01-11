package gui.controllers;

import be.Category;
import bll.CategoryManager;
import javafx.fxml.FXML;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

public class EditCategoryController {
    @FXML
    protected TextField editCategoryName;
    private MainWindowController mainWindowController;
    private Category selectedCategory;
    private CategoryManager categoryManager = new CategoryManager();


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
                categoryManager.updateCategory(newCategory, selectedCategory.getName().get());
                mainWindowController.editCategory(newCategory, selectedCategory);
                closeEditedWindow();
            }
        }
    }
    private void closeEditedWindow(){
        Stage stage = (Stage) editCategoryName.getScene().getWindow();
        stage.close();
    }

    public void cancelEditCategoryButton(){
        closeEditedWindow();
    }
}

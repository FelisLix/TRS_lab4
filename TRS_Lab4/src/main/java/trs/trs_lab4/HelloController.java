package trs.trs_lab4;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import trs.trs_lab4.models.Product;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class HelloController {

    @FXML
    public ChoiceBox<String> addType;
    public TextField editPrice;
    public TextField editQuantity;
    public Button saveProduct;
    public ChoiceBox<String> editType;
    public TextField editName;
    @FXML
    private TableView<Product> tableProducts;
    @FXML
    private TableColumn<Product, Integer> colID;
    @FXML
    private TableColumn<Product, String> colName;
    @FXML
    private TableColumn<Product, Double> colPrice;
    @FXML
    private TableColumn<Product, Integer> colQuantity;
    @FXML
    private TableColumn<Product, String> colCreatedAt;
    @FXML
    public TableColumn<Product, String> colType;
    public TextField addNewName;
    public TextField addNewPrice;
    public TextField addNewQuantity;
    public Button addNewProduct;
    private List<Integer> typeList;
    @FXML
    private TableColumn<Product, Void> colActions;

    public void initialize() {
        colID.setCellValueFactory(cellData -> cellData.getValue().idProperty().asObject());
        colName.setCellValueFactory(cellData -> cellData.getValue().nameProperty());
        colPrice.setCellValueFactory(cellData -> cellData.getValue().priceProperty().asObject());
        colQuantity.setCellValueFactory(cellData -> cellData.getValue().quantityProperty().asObject());
        colCreatedAt.setCellValueFactory(cellData -> cellData.getValue().createdAtProperty());
        colType.setCellValueFactory(cellData -> cellData.getValue().typeProperty());
        colActions.setCellFactory(param -> new TableCell<>() {
            private final Button deleteButton = new Button("Delete");

            {
                deleteButton.setOnAction(event -> {
                    Product product = getTableView().getItems().get(getIndex());
                    handleDeleteProductFromRow(product);
                });
            }
            @Override
            protected void updateItem(Void item, boolean empty) {
                super.updateItem(item, empty);
                if (empty) {
                    setGraphic(null);
                } else {
                    setGraphic(deleteButton);
                }
            }
        });
        loadData();
        addTypesFromDatabase();
    }

    @FXML
    protected void loadData() {
        ObservableList<Product> productList = FXCollections.observableArrayList();
        try (Connection connection = DatabaseConnection.getConnection()) {
            String query = "select product.product_id, product.name, product.price, product.quantity, " +
                    "product.created_at, t.name from product\n" +
                    "join aquarium.type t on t.type_id = product.type_id";
            Statement statement = connection.createStatement();
            ResultSet resultSet = statement.executeQuery(query);

            while (resultSet.next()) {
                int id = resultSet.getInt("product.product_id");
                String name = resultSet.getString("product.name");
                double price = resultSet.getDouble("product.price");
                int quantity = resultSet.getInt("product.quantity");
                String createdAt = resultSet.getString("product.created_at");
                String type = resultSet.getString("t.name");

                Product product = new Product(id, name, price, quantity, createdAt, type);
                productList.add(product);
            }

            tableProducts.setItems(productList);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void deleteProductFromDatabase(int productId) {
        String query = "DELETE FROM product WHERE product_id = ?";

        try (Connection connection = DatabaseConnection.getConnection();
             PreparedStatement statement = connection.prepareStatement(query)) {

            statement.setInt(1, productId);

            int rowsDeleted = statement.executeUpdate();
            if (rowsDeleted > 0) {
                System.out.println("Product deleted successfully.");
            } else {
                System.out.println("Product not found in the database.");
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    private void handleDeleteProductFromRow(Product product) {
        deleteProductFromDatabase(product.getId());

        tableProducts.getItems().remove(product);
    }

    public void addTypesFromDatabase() {
        ObservableList<String> options = FXCollections.observableArrayList();
        typeList = new ArrayList<>();

        String query = "select * from type";
        try (Connection connection = DatabaseConnection.getConnection();
             Statement statement = connection.createStatement();
             ResultSet resultSet = statement.executeQuery(query)) {

            while (resultSet.next()) {
                typeList.add(resultSet.getInt("type_id"));
                options.add(resultSet.getString("name"));
            }
            addType.setItems(options);
            editType.setItems(options);

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }


    @FXML
    public void handleAddNewProduct() {
        String name = addNewName.getText();
        double price;
        int quantity;
        int typeID = addType.getSelectionModel().getSelectedIndex();

        try {
            price = Double.parseDouble(addNewPrice.getText());
            quantity = Integer.parseInt(addNewQuantity.getText());
        } catch (NumberFormatException e) {
            System.out.println("Invalid input for price or quantity.");
            return;
        }

        Product newProduct = new Product(0, name, price, quantity, "default", "default");

        addProductToDatabase(newProduct, typeID);

        loadData();

        addNewName.clear();
        addNewPrice.clear();
        addNewQuantity.clear();
    }

    public void addProductToDatabase(Product product, int typeID) {
        String query = "INSERT INTO product (name, price, quantity, created_at, type_id) VALUES (?, ?, ?, ?, ?)";

        try (Connection connection = DatabaseConnection.getConnection();
             PreparedStatement statement = connection.prepareStatement(query)) {

            statement.setString(1, product.getName());
            statement.setDouble(2, product.getPrice());
            statement.setInt(3, product.getQuantity());
            Timestamp createdAt = new Timestamp(System.currentTimeMillis());
            statement.setString(4, String.valueOf(createdAt));  // Using default created date for now
            statement.setInt(5, typeID + 1);

            int rowsInserted = statement.executeUpdate();
            if (rowsInserted > 0) {
                System.out.println("A new product was inserted successfully!");
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }


    @FXML
    public void handleEditProduct() {
        Product selectedProduct = tableProducts.getSelectionModel().getSelectedItem();

        if (selectedProduct == null) {
            System.out.println("No product selected for editing.");
            return;
        }

        editName.setText(selectedProduct.getName());
        editPrice.setText(String.valueOf(selectedProduct.getPrice()));
        editQuantity.setText(String.valueOf(selectedProduct.getQuantity()));
    }



    @FXML
    public void handleSaveProduct() {

        Product selectedProduct = tableProducts.getSelectionModel().getSelectedItem();

        if (selectedProduct == null) {
            System.out.println("No product selected for saving changes.");
            return;
        }

        String updatedName = editName.getText();
        double updatedPrice;
        int updatedQuantity;

        try {
            updatedPrice = Double.parseDouble(editPrice.getText());
            updatedQuantity = Integer.parseInt(editQuantity.getText());
        } catch (NumberFormatException e) {
            System.out.println("Invalid input for price or quantity.");
            return;
        }

        if (editType.getSelectionModel().isEmpty()){
            updateProductInDatabase(selectedProduct.getId(), updatedName, updatedPrice, updatedQuantity, 0);
        } else {
            int type = editType.getSelectionModel().getSelectedIndex();
            updateProductInDatabase(selectedProduct.getId(), updatedName, updatedPrice, updatedQuantity, type+1);
        }

        loadData();
    }

    public void updateProductInDatabase(int productId, String name, double price, int quantity, int type) {
        String query = type == 0
                ? "UPDATE product SET name = ?, price = ?, quantity = ? WHERE product_id = ?"
                : "UPDATE product SET name = ?, price = ?, quantity = ?, type_id = ? WHERE product_id = ?";

        try (Connection connection = DatabaseConnection.getConnection();
             PreparedStatement statement = connection.prepareStatement(query)) {

            statement.setString(1, name);
            statement.setDouble(2, price);
            statement.setInt(3, quantity);

            if (type != 0) {
                statement.setInt(4, type);
                statement.setInt(5, productId);
            } else {
                statement.setInt(4, productId);
            }

            int rowsUpdated = statement.executeUpdate();
            if (rowsUpdated > 0) {
                System.out.println("Product updated successfully.");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}

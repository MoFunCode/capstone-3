package org.yearup.data.mysql;

import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.PathVariable;
import org.yearup.data.CategoryDao;
import org.yearup.models.Category;

import javax.sql.DataSource;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

@Component
public class MySqlCategoryDao extends MySqlDaoBase implements CategoryDao {
    public MySqlCategoryDao(DataSource dataSource) {
        super(dataSource);
    }

    @Override
    public List<Category> getAllCategories() {

        // get all categories
        List<Category> categories = new ArrayList<>();

        try (Connection con = getConnection();
             PreparedStatement preparedStatement = con.prepareStatement("SELECT * FROM categories");
             ResultSet rs = preparedStatement.executeQuery()) {

            while (rs.next()) {
                int id = rs.getInt("category_id");
                String name = rs.getString("name");
                String description = rs.getString("description");

                Category category = new Category(id, name, description);
                categories.add(category);
            }

        } catch (SQLException e) {
            throw new RuntimeException(e);
        }

        return categories;
    }

    @Override
    public Category getById(int categoryId) {
        // get category by id

        try (Connection con = getConnection();
             PreparedStatement preparedStatement = con.prepareStatement("SELECT * FROM categories WHERE category_id = ?")) {

            preparedStatement.setInt(1, categoryId);

            try (ResultSet rs = preparedStatement.executeQuery()) {
                if (rs.next()) {
                    int id = rs.getInt("category_id");
                    String name = rs.getString("name");
                    String description = rs.getString("description");

                    return new Category(id, name, description);
                }
            }

        } catch (SQLException e) {
            throw new RuntimeException(e);
        }

        return null;
    }

    /**
     * Only administrators (users with the ADMIN role) should be allowed to insert,
     * update or delete a category.
     * @param category
     * @return
     */
    @Override
    public Category create(Category category) {
        // create a new category

        String sql = "INSERT INTO categories (name, description) VALUES (?, ?)";

        try (Connection con = getConnection();
             PreparedStatement preparedStatement = con.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {

            preparedStatement.setString(1, category.getName());
            preparedStatement.setString(2, category.getDescription());

            int rowsAffected = preparedStatement.executeUpdate();
            System.out.printf("Rows updated %d\n", rowsAffected);

            if (rowsAffected > 0) {

                try (ResultSet generatedKeys = preparedStatement.getGeneratedKeys()) {
                    if (generatedKeys.next()) {
                        int generatedId = generatedKeys.getInt(1);
                        category.setCategoryId(generatedId);
                        return category;
                    }
                }
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return null; // Insert failed
    }

    @Override
    public void update(int categoryId, Category category) {
        // update category

        String sql = "UPDATE categories SET name = ?, description = ? where category_id = ?";

        try (Connection con = getConnection();
             PreparedStatement preparedStatement = con.prepareStatement(sql)) {

            preparedStatement.setString(1, category.getName());
            preparedStatement.setString(2, category.getDescription());
            preparedStatement.setInt(3, categoryId);

            int rowsAffected = preparedStatement.executeUpdate();
            System.out.printf("Rows updated: %d\n", rowsAffected);

            if (rowsAffected > 0) {
                System.out.println("Category updated successfully!");
            } else {
                System.out.println("I could not your category: " + category.getCategoryId());
            }


        } catch (SQLException e) {
            System.out.println("Error updating you're category" + e.getMessage());
            e.printStackTrace();
        }
    }

    @Override
    public void delete(int categoryId)
    {
        // delete category
    }

    private Category mapRow(ResultSet row) throws SQLException
    {
        int categoryId = row.getInt("category_id");
        String name = row.getString("name");
        String description = row.getString("description");

        Category category = new Category()
        {{
            setCategoryId(categoryId);
            setName(name);
            setDescription(description);
        }};

        return category;
    }

}

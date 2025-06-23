package org.yearup.data.mysql;

import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.PathVariable;
import org.yearup.data.CategoryDao;
import org.yearup.models.Category;

import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
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
    public Category create(Category category)
    {
        // create a new category
        return null;
    }

    @Override
    public void update(int categoryId, Category category)
    {
        // update category
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

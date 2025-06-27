package org.yearup.data;

import org.yearup.models.ShoppingCart;

public interface ShoppingCartDao
{
    ShoppingCart getByUserId(int userId);
    void save(int userId, ShoppingCart cart);
    void clearCartByUserId(int userId);

}



package ch.heigvd.digiback.business.api.category;

import ch.heigvd.digiback.business.api.iOnDataFetched;
import ch.heigvd.digiback.business.model.category.Category;

public interface iOnCategoryFetched extends iOnDataFetched<Category> {
    void showProgressBar();

    void hideProgressBar();

    void setDataInPageWithResult(Category category);
}

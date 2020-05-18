package id.ac.umn.simplymeal.CategoryView;

public class CategoryTab {

    private String idCategory;
    private String image;
    //    private Map<String, Object> imageMenu ;
    private String categoryName;
    private String desc;

    public  CategoryTab(){

    }

    public String getIdCategory() {
        return idCategory;
    }

    public void setIdCategory(String idCategory) {
        this.idCategory = idCategory;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public String getCategoryName() {
        return categoryName;
    }

    public void setCategoryName(String categoryName) {
        this.categoryName = categoryName;
    }

    public String getDesc() {
        return desc;
    }

    public void setDesc(String desc) {
        this.desc = desc;
    }
}

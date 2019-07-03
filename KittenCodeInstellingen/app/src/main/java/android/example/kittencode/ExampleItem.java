package android.example.kittencode;

//Verander naam van class.
public class ExampleItem {
    private String mImageUrl;
    private String mCreator;
    private int mLikes;

    public ExampleItem(String imageUrl, String creator, int likes){
        //De parameters die zijn meegestuurd staan gelijk aan de variabeles die je hebt aangemaakt.
        mImageUrl = imageUrl;
        mCreator = creator;
        mLikes = likes;
    }

    //Create method om de data in de variabels te halen
    public String getImageUrl(){
        return mImageUrl;
    }

    public String getCreator(){
        return mCreator;
    }

    public int getLikeCount(){
        return mLikes;
    }
}

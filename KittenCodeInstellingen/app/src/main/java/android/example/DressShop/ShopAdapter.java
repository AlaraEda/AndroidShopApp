package android.example.DressShop;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import java.util.ArrayList;

public class ShopAdapter extends RecyclerView.Adapter<ShopAdapter.ExampleViewHolder> {
    private Context mContext;
    private ArrayList<ShopItem> mExampleList;

    //Click variabel
    private OnItemClickListener mListener;

    //Click-Handler
    public interface OnItemClickListener{
        void onItemClick(int position);
    }

    public void setOnItemClickListener(OnItemClickListener  listener){
        mListener = listener;
    }

    //Constructor voor adapter
    public ShopAdapter(Context context, ArrayList<ShopItem> exampleList){
        //variabelen krijgt de inhoud van de parameters
        mContext = context;
        mExampleList = exampleList;
    }


    @Override
    public ExampleViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        //Return ViewHolder
        View v = LayoutInflater.from(mContext).inflate(R.layout.example_item, parent, false);
        return new ExampleViewHolder(v);
    }

    //Word opgeroepen elke keer dat de viewer scrollt
    @Override
    public void onBindViewHolder(ExampleViewHolder holder, int position) {
        //Gebruik de int position om items uit de array-list te halen
        ShopItem currentItem = mExampleList.get(position);

        //Gebruik de "CurrentItem variabele" om imageurl en text eruit te krijgen
        //We hadden een class aangemaakt met ShopItem en daar halen we de variabels weer uit.
        String imageUrl = currentItem.getImageUrl();
        String creatorName = currentItem.getCreator();
        int likeCount = currentItem.getLikeCount();

        //Zet de values die je hebt gekregen in de kaart
        holder.mTextViewCreator.setText(creatorName);
        holder.mTextViewLikes.setText("Likes: " + likeCount);
        //Fit()zodat het precies goed de image past. into()waar de image precies in moet zitten.
        Picasso.with(mContext).load(imageUrl).fit().centerInside().into(holder.mImageView);


    }

    @Override
    public int getItemCount() {
        //Ik wil dezelfde hoeveelheid items in mijn adapter als in mijn arrayList
        return mExampleList.size();
    }

    //New holder class, dat een verleng stuk is van
    public class ExampleViewHolder extends RecyclerView.ViewHolder{
        //Variabellen voor de View's
        public ImageView mImageView;
        public TextView mTextViewCreator;
        public TextView mTextViewLikes;

        //De itemView kunnen we gebruiken om views te krijgen van onze 1 image & 2 text-views.
        //De onclickListener staat op de Itemview
        public ExampleViewHolder(@NonNull View itemView) {
            super(itemView);

            //Het vinden van de variabellen.
            mImageView = itemView.findViewById(R.id.image_view);
            mTextViewCreator = itemView.findViewById(R.id.text_view_creator);
            mTextViewLikes = itemView.findViewById(R.id.text_view_likes);

            //ClickListener
            itemView.setOnClickListener(new View.OnClickListener(){
                @Override
                public void onClick(View v){
                    //Dus alleen als je een listener hebt doe je dit.
                    if(mListener != null){
                        int position = getAdapterPosition();
                        //Zorgt ervoor dat de positie op de recyleview staat
                        if (position != RecyclerView.NO_POSITION){
                            mListener.onItemClick(position);
                        }
                    }
                }
            });
        }
    }
}

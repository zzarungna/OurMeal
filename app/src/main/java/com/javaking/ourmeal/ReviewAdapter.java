package com.javaking.ourmeal;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.RequestManager;
import com.javaking.ourmeal.model.Star_bulletin;

import java.util.ArrayList;

public class ReviewAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    //private static final String IP = "http://172.30.1.37:8080";//학원
    private static final String IP = "http://192.168.0.17:8080";//학원

        public static class ReviewHolder extends RecyclerView.ViewHolder {
            ImageView sb_score;
            ImageView sb_image;
            ImageView member_image;
            TextView member_id;
            TextView sb_u_date;
            TextView sb_content;





            ReviewHolder(View view){
                super(view);
                sb_score = view.findViewById(R.id.sb_score);
                sb_image = view.findViewById(R.id.sb_image);
                member_image = view.findViewById(R.id.member_image);
                sb_u_date = view.findViewById(R.id.sb_u_date);
                sb_content = view.findViewById(R.id.sb_content);
                member_id = view.findViewById(R.id.member_id);
}
        }

        private ArrayList<Star_bulletin> reviewList;
        private RequestManager mGlideRequestManager;
    ReviewAdapter(ArrayList<Star_bulletin> reviewList, RequestManager mGlideRequestManager){
            this.reviewList = reviewList;
            this.mGlideRequestManager = mGlideRequestManager;
        }

        @Override
        public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

            View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.recycler_view_row, parent, false);

            return new ReviewHolder(v);
        }

        @Override
        public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {

            ReviewHolder reviewHolder = (ReviewHolder) holder;

            mGlideRequestManager.load(IP+"/OurMeal/"+reviewList.get(position).getSb_image()).into(reviewHolder.sb_image);



           // Glide.with().load("http://172.30.1.17:8080/OurMeal/"+reviewList.get(position).getSb_image()).into(sb_image);

            // reviewHolder.sb_image.setImageResource(reviewList.get(position).getSb_image());


            reviewHolder.member_id.setText(reviewList.get(position).getMember_id());



                if(reviewList.get(position).getSb_score().equals("1.0"))
                    reviewHolder.sb_score.setImageResource(R.drawable.star_1);

                    else if(reviewList.get(position).getSb_score().equals("2.0"))
                    reviewHolder.sb_score.setImageResource(R.drawable.star_2);

                     else if(reviewList.get(position).getSb_score().equals("3.0"))
                    reviewHolder.sb_score.setImageResource(R.drawable.star_3);

                     else if(reviewList.get(position).getSb_score().equals("4.0"))
                    reviewHolder.sb_score.setImageResource(R.drawable.star_4);
                     else
                    reviewHolder.sb_score.setImageResource(R.drawable.star_5);


            if ((reviewList.get(position).getMember_image()).trim().equals(""))
            reviewHolder.member_image.setImageResource(R.drawable.st);
            else
            mGlideRequestManager.load(IP+"/OurMeal/"+reviewList.get(position).getMember_image()).into(reviewHolder.member_image);


            reviewHolder.sb_u_date.setText(reviewList.get(position).getSb_u_date());

            reviewHolder.sb_content.setText(reviewList.get(position).getSb_content());

            reviewHolder.member_id.setText(reviewList.get(position).getMember_id());






    }

        @Override
        public int getItemCount() {
            return reviewList.size();
        }







}

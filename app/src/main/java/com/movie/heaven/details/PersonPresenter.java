package com.movie.heaven.details;

import android.content.Context;
import android.view.ContextThemeWrapper;
import android.view.ViewGroup;
import com.movie.heaven.R;
import androidx.leanback.widget.ImageCardView;
import androidx.leanback.widget.Presenter;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.movie.heaven.dagger.modules.HttpClientModule;
import com.movie.heaven.data.models.CastMember;

public class PersonPresenter extends Presenter {

    Context mContext;

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent) {

        if (mContext == null) {
            // We do this to avoid creating a new ContextThemeWrapper for each one of the cards
            // If we look inside the ImageCardView they warn us about the same this.
            // Example: Try using the constructor: ImageCardView(context, style)
            // It is deprecated right? This is because that constructor creates a new ContextThemeWrapper every time a
            // ImageCardView is allocated.
            mContext = new ContextThemeWrapper(parent.getContext(), R.style.PersonCardTheme);
        }

        return new ViewHolder(new ImageCardView(mContext));
    }

    @Override
    public void onBindViewHolder(ViewHolder viewHolder, Object item) {
        ImageCardView view = (ImageCardView) viewHolder.view;
        CastMember member = (CastMember) item;

        Glide.with(view.getContext())
                .load(HttpClientModule.POSTER_URL + member.getProfilePath())
                .diskCacheStrategy(DiskCacheStrategy.ALL)
                .into(view.getMainImageView());

        view.setTitleText(member.getName());
        view.setContentText(member.getCharacter());
    }

    @Override
    public void onUnbindViewHolder(ViewHolder viewHolder) {

    }
}
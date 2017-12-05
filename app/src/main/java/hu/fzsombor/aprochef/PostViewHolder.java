/*
 * Copyright 2016 Google Inc. All Rights Reserved.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package hu.fzsombor.aprochef;

import android.content.Context;
import android.content.Intent;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.ValueEventListener;

public class PostViewHolder extends RecyclerView.ViewHolder {
    private static final int POST_TEXT_MAX_LINES = 6;
    private final View mView;
    private final ImageView mLikeIcon;
    public DatabaseReference mPostRef;
    public ValueEventListener mPostListener;
    public ValueEventListener mLikeListener;
    private PostClickListener mListener;
    private ImageView mPhotoView;
    private ImageView mIconView;
    private TextView mAuthorView;
    private TextView mPostNameView;
    private TextView mPostShortView;
    private TextView mTimestampView;
    private TextView mNumLikesView;
    private String mPostKey;

    public PostViewHolder(View itemView) {
        super(itemView);
        mView = itemView;
        mPhotoView = (ImageView) itemView.findViewById(R.id.post_photo);
        mIconView = (ImageView) mView.findViewById(R.id.post_author_icon);
        mAuthorView = (TextView) mView.findViewById(R.id.post_author_name);
        mPostNameView = (TextView) itemView.findViewById(R.id.post_name);
        mPostShortView = (TextView) itemView.findViewById(R.id.post_short);
        mTimestampView = (TextView) itemView.findViewById(R.id.post_timestamp);
        mNumLikesView = (TextView) itemView.findViewById(R.id.post_num_likes);

        itemView.findViewById(R.id.post_comment_icon).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mListener.showComments();
            }
        });
        mLikeIcon = (ImageView) itemView.findViewById(R.id.post_like_icon);
        mLikeIcon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mListener.toggleLike();
            }
        });
    }

    public void setPhoto(String url) {
        GlideUtil.loadImage(url, mPhotoView);
        mPhotoView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showDetail(mPostKey);
            }
        });
    }

    public void setIcon(String url, final String authorId) {
        GlideUtil.loadProfileIcon(url, mIconView);
        mIconView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showUserDetail(authorId);
            }
        });
    }

    public void setAuthor(String author, final String authorId) {
        if (author == null || author.isEmpty()) {
            author = mView.getResources().getString(R.string.user_info_no_name);
        }
        mAuthorView.setText(author);
        mAuthorView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showUserDetail(authorId);
            }
        });
    }

    private void showUserDetail(String authorId) {
        Context context = mView.getContext();
        Intent userDetailIntent = new Intent(context, UserDetailActivity.class);
        userDetailIntent.putExtra(UserDetailActivity.USER_ID_EXTRA_NAME, authorId);
        context.startActivity(userDetailIntent);
    }

    private void showDetail(String postId) {
        Context context = mView.getContext();
        Intent detailIntent = new Intent(context, DetailActivity.class);
        detailIntent.putExtra(DetailActivity.USER_ID_EXTRA_NAME, postId);
        context.startActivity(detailIntent);
    }

    public void setName(final String name) {
        if (name == null || name.isEmpty()) {
            mPostNameView.setVisibility(View.GONE);
            return;
        } else {
            mPostNameView.setVisibility(View.VISIBLE);
            mPostNameView.setText(name);
            mPostNameView.setMaxLines(POST_TEXT_MAX_LINES);
            mPostNameView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if (mPostNameView.getMaxLines() == POST_TEXT_MAX_LINES) {
                        mPostNameView.setMaxLines(Integer.MAX_VALUE);
                    } else {
                        mPostNameView.setMaxLines(POST_TEXT_MAX_LINES);
                    }
                }
            });
        }
    }

    public void setShort(final String text) {
        if (text == null || text.isEmpty()) {
            mPostShortView.setVisibility(View.GONE);
            return;
        } else {
            mPostShortView.setVisibility(View.VISIBLE);
            mPostShortView.setText(text);
            mPostShortView.setMaxLines(POST_TEXT_MAX_LINES);
            mPostShortView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if (mPostShortView.getMaxLines() == POST_TEXT_MAX_LINES) {
                        mPostShortView.setMaxLines(Integer.MAX_VALUE);
                    } else {
                        mPostShortView.setMaxLines(POST_TEXT_MAX_LINES);
                    }
                }
            });
        }
    }

    public void setTimestamp(String timestamp) {
        mTimestampView.setText(timestamp);
    }

    public void setmPostKey(String mPostKey) {
        this.mPostKey = mPostKey;
    }

    public void setNumLikes(long numLikes) {
        String suffix = numLikes == 1 ? " like" : " likes";
        mNumLikesView.setText(numLikes + suffix);
    }

    public void setPostClickListener(PostClickListener listener) {
        mListener = listener;
    }

    public void setLikeStatus(LikeStatus status, Context context) {
        mLikeIcon.setImageDrawable(ContextCompat.getDrawable(context,
                status == LikeStatus.LIKED ? R.drawable.heart_full : R.drawable.heart_empty));
    }

    public enum LikeStatus {LIKED, NOT_LIKED}


    public interface PostClickListener {
        void showComments();

        void toggleLike();
    }
}
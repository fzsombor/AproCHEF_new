package hu.fzsombor.aprochef;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.ValueEventListener;

import hu.fzsombor.aprochef.Models.Post;

/**
 * Created by zsomb on 2017. 12. 05..
 */

public class DetailActivity extends AppCompatActivity {
    public static final String USER_ID_EXTRA_NAME = "post_name";
    private static final int GRID_NUM_COLUMNS = 2;
    private final String TAG = "DetailActivity";
    private RecyclerView mRecyclerGrid;
    private hu.fzsombor.aprochef.UserDetailActivity.GridAdapter mGridAdapter;
    private ValueEventListener mFollowingListener;
    private ValueEventListener mPostListener;
    private String mPostId;
    private DatabaseReference mPeopleRef;
    private DatabaseReference mPostRef;
    private DatabaseReference mFollowersRef;
    private ValueEventListener mFollowersListener;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_post_detail);

        Intent intent = getIntent();
        mPostId = intent.getStringExtra(USER_ID_EXTRA_NAME);

        final Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        final CollapsingToolbarLayout collapsingToolbar =
                (CollapsingToolbarLayout) findViewById(R.id.collapsing_toolbar);
        // TODO: Investigate why initial toolbar title is activity name instead of blank.


        mPostRef = FirebaseUtil.getPostsRef().child(mPostId);
        mPostListener = mPostRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                Post post = dataSnapshot.getValue(Post.class);
                Log.w(TAG, "mPostRef:" + mPostRef.getKey());
                //CircleImageView userPhoto = (CircleImageView) findViewById(R.id.user_detail_photo);
                //GlideUtil.loadProfileIcon(person.getPhotoUrl(), userPhoto);
                String name = post.getName();
                if (name == null) {
                    name = getString(R.string.user_info_no_name);
                }
                collapsingToolbar.setTitle(name);
                ImageView imageView = (ImageView) findViewById(R.id.detail_backdrop);
                GlideUtil.loadImage(post.getFull_url(), imageView);
                TextView shortDescription = (TextView) findViewById(R.id.detail_short);
                shortDescription.setText(post.getShortDescription());
                TextView longDescription = (TextView) findViewById(R.id.detail_long);
                longDescription.setText(post.getLongDescription());


            }

            @Override
            public void onCancelled(DatabaseError firebaseError) {

            }
        });
    }

    @Override
    protected void onDestroy() {

        mPostRef.removeEventListener(mPostListener);
        super.onDestroy();
    }


    @Override
    public boolean onSupportNavigateUp() {
        finish();
        return super.onSupportNavigateUp();
    }
}
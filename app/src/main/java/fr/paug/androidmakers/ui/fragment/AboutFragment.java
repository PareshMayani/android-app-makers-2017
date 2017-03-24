package fr.paug.androidmakers.ui.fragment;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.customtabs.CustomTabsIntent;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;

import java.util.Map;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Unbinder;
import fr.paug.androidmakers.R;
import fr.paug.androidmakers.manager.AgendaRepository;
import fr.paug.androidmakers.model.PartnerGroup;
import fr.paug.androidmakers.model.Partners;

public class AboutFragment extends Fragment {

    @BindView(R.id.about_layout) LinearLayout aboutLayout;
    private Unbinder unbinder;

    public AboutFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_about, container, false);
        unbinder = ButterKnife.bind(this, view);

        Map<PartnerGroup.PartnerType, PartnerGroup> partners = AgendaRepository.getInstance().getPartners();
        for (PartnerGroup.PartnerType partnerType : partners.keySet()) {
            Log.d("AboutFragment", partnerType.toString() + ", " + partners.get(partnerType).getPartnersList().toString());

            LinearLayout partnersGroupLinearLayout = (LinearLayout) LayoutInflater.from(getContext()).inflate(R.layout.partners_group, null);
            TextView textView = (TextView) partnersGroupLinearLayout.findViewById(R.id.partners_title);
            textView.setText(partnerType.name());
            aboutLayout.addView(partnersGroupLinearLayout);

            for (final Partners partner : partners.get(partnerType).getPartnersList()) {
                ImageView imageView = (ImageView) LayoutInflater.from(getContext()).inflate(R.layout.partner, null);
                Glide.with(getContext())
                        .load("http://androidmakers.fr/img/partners/" + partner.getImageUrl())
                        .into(imageView);
                imageView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        CustomTabsIntent.Builder builder = new CustomTabsIntent.Builder();
                        builder.setToolbarColor(ContextCompat.getColor(getContext(), R.color.colorPrimary));
                        CustomTabsIntent customTabsIntent = builder.build();
                        customTabsIntent.launchUrl(getContext(), Uri.parse(partner.getLink()));
                    }
                });
                partnersGroupLinearLayout.addView(imageView);
            }
        }

        return view;
    }

    @OnClick(R.id.twitter_user_button)
    void openTwitterUser() {
        Intent twitterIntent;
        try {
            // get the Twitter app if possible
            getActivity().getPackageManager().getPackageInfo("com.twitter.android", 0);
            twitterIntent = new Intent(Intent.ACTION_VIEW, Uri.parse("twitter://user?screen_name=AndroidMakersFR"));
            twitterIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        } catch (Exception e) {
            // no Twitter app, revert to browser
            twitterIntent = new Intent(Intent.ACTION_VIEW, Uri.parse("https://twitter.com/AndroidMakersFR"));
        }
        startActivity(twitterIntent);
    }

    @OnClick(R.id.twitter_hashtag_button)
    void openTwitterHashtag() {
        Intent twitterIntent;
        try {
            // get the Twitter app if possible
            getActivity().getPackageManager().getPackageInfo("com.twitter.android", 0);
            twitterIntent = new Intent(Intent.ACTION_VIEW, Uri.parse("twitter://search?query=%23AndroidMakersFR"));
            twitterIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        } catch (Exception e) {
            // no Twitter app, revert to browser
            twitterIntent = new Intent(Intent.ACTION_VIEW, Uri.parse("https://twitter.com/search?q=%23AndroidMakersFR"));
        }
        startActivity(twitterIntent);
    }

    @OnClick(R.id.google_plus_button)
    void openGPlus() {
        Intent gplusIntent = new Intent(Intent.ACTION_VIEW, Uri.parse(getString(R.string.gplus)));
        startActivity(gplusIntent);
    }

    @OnClick(R.id.facebook_button)
    void openFacebookEvent() {
        Intent facebookIntent = new Intent(Intent.ACTION_VIEW, Uri.parse(getString(R.string.fbevent)));
        startActivity(facebookIntent);
    }

    @OnClick(R.id.youtube_button)
    void openYoutube() {
        Intent ytIntent = new Intent(Intent.ACTION_VIEW, Uri.parse(getString(R.string.ytchannel)));
        startActivity(ytIntent);
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        unbinder.unbind();
    }

}
package com.mrntlu.myanimeinfo2.ui.others

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.mrntlu.myanimeinfo2.R
import com.vansuita.materialabout.builder.AboutBuilder
import com.vansuita.materialabout.views.AboutView
import kotlinx.android.synthetic.main.fragment_about_me.*

class AboutMeFragment : Fragment() {

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_about_me, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val aboutView: AboutView = AboutBuilder.with(view.context)
            .setPhoto(R.mipmap.profile_picture)
            .setCover(R.mipmap.profile_cover)
            .setName("MrNtlu")
            .setSubTitle("Mobile Developer")
            .addLinkedInLink("burak-fidan")
            .setAppIcon(R.mipmap.ic_launcher)
            .setAppName(R.string.app_name)
            .addGooglePlayStoreLink("") //TODO Change
            .addGitHubLink("MrNtlu")
            .addEmailLink("mrntlu@gmail.com")
            .addWebsiteLink("https://burakfidan.net/")
            .addFiveStarsAction()
            .setVersionNameAsAppSubTitle()
            .addFeedbackAction("mrntlu@gmail.com")
            .addMoreFromMeAction("MrNtlu")
            .addShareAction(R.string.app_name)
            .setWrapScrollView(true)
            .setLinksAnimated(true)
            .setShowAsCard(true)
            .build()
        aboutMeFrameLayout.addView(aboutView)
    }
}

package com.mrntlu.myanimeinfo2.ui.common

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.mrntlu.myanimeinfo2.R
import com.mrntlu.myanimeinfo2.models.PictureBodyResponse
import com.mrntlu.myanimeinfo2.utils.loadWithGlide
import kotlinx.android.synthetic.main.fragment_picture_item.*

class PictureItemFragment(private val pictureBodyResponse: PictureBodyResponse) : Fragment() {

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_picture_item, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        pictureImage.loadWithGlide(pictureBodyResponse.large,pictureProgressBar)
    }
}

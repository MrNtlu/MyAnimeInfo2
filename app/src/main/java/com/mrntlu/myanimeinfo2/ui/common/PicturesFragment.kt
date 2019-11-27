package com.mrntlu.myanimeinfo2.ui.common

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import androidx.navigation.NavController
import androidx.navigation.Navigation
import com.mrntlu.myanimeinfo2.R
import com.mrntlu.myanimeinfo2.adapters.pageradapters.PicturesPagerAdapter
import com.mrntlu.myanimeinfo2.interfaces.CoroutinesErrorHandler
import com.mrntlu.myanimeinfo2.models.DataType
import com.mrntlu.myanimeinfo2.models.PictureBodyResponse
import com.mrntlu.myanimeinfo2.utils.printLog
import com.mrntlu.myanimeinfo2.utils.setGone
import com.mrntlu.myanimeinfo2.utils.setVisible
import com.mrntlu.myanimeinfo2.viewmodels.CommonViewModel
import kotlinx.android.synthetic.main.cell_error.view.*
import kotlinx.android.synthetic.main.fragment_pictures.*
import kotlinx.android.synthetic.main.fragment_pictures.errorLayout
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import java.util.*
import kotlin.properties.Delegates
import android.graphics.Bitmap
import android.graphics.drawable.BitmapDrawable
import android.graphics.drawable.Drawable
import android.media.MediaScannerConnection
import kotlinx.android.synthetic.main.fragment_picture_item.*
import java.io.File
import java.io.FileOutputStream
import java.lang.Exception
import android.os.Build
import android.provider.MediaStore

class PicturesFragment : Fragment(), CoroutinesErrorHandler {

    private lateinit var commonViewModel: CommonViewModel
    private var malID by Delegates.notNull<Int>()
    private lateinit var navController: NavController
    private lateinit var dataType: DataType
    private lateinit var pictures:List<PictureBodyResponse>
    private lateinit var fragments: ArrayList<PictureItemFragment>

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            malID=it.getInt("mal_id")
            dataType=DataType.getByCode(it.getInt("data_type"))
        }
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_pictures, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        navController= Navigation.findNavController(view)
        commonViewModel= ViewModelProviders.of(this).get(CommonViewModel::class.java)

        setupObservers()
        setListeners()
    }

    private fun setListeners(){
        errorLayout.errorRefreshButton.setOnClickListener {
            downloadFab.setVisible()
            errorLayout.setGone()
            setupObservers()
        }

        downloadFab.setOnClickListener {
            if (::fragments.isInitialized){
                //TODO ask for permission
                downloadImage(it,fragments[pictureViewpager.currentItem].pictureImage.drawable)
            }
        }
    }

    @Suppress("DEPRECATION")
    private fun downloadImage(view:View, drawable: Drawable) {
        val bitmap = (drawable as BitmapDrawable).bitmap
        var fout: FileOutputStream? =null
        var imageMainDirectory:File?=null
        val root:File?

        try {
            root=File(view.context.getExternalFilesDir(null)!!.absolutePath)
            root.mkdirs()
            imageMainDirectory=File(root,"mPic.jpg")
            fout=FileOutputStream(imageMainDirectory)

            printLog(message = "Paths $root $imageMainDirectory ${view.context.externalMediaDirs} ${view.context.getExternalFilesDir(null)!!.absolutePath}")
        }catch (e:Exception){
            e.printStackTrace()
        }
        try {
            bitmap.compress(Bitmap.CompressFormat.JPEG,100,fout)
            fout?.let {
                it.flush()
                it.close()
            }
            if (Build.VERSION.SDK_INT == Build.VERSION_CODES.Q ){
                MediaScannerConnection.scanFile(context, arrayOf(imageMainDirectory!!.path), arrayOf("image/jpeg","image/png")) { _, uri->
                    MediaStore.setIncludePending(uri)
                }
            }else{
                MediaStore.Images.Media.insertImage(view.context.contentResolver, bitmap, "Test", "Desc")
            }
        }catch (e:Exception){
            e.printStackTrace()
        }
    }

    private fun setupObservers() {
        commonViewModel.getPicturesByID(dataType.toString().toLowerCase(Locale.ENGLISH),malID,this).observe(viewLifecycleOwner, Observer {
            pictures=it.pictures
            setupViewPagers(it.pictures)
        })
    }

    private fun setupViewPagers(pictures: List<PictureBodyResponse>) {
        fragments= arrayListOf()
        for (picture in pictures){
            fragments.add(PictureItemFragment(picture))
        }
        val pagerAdapter=PicturesPagerAdapter(childFragmentManager,fragments)
        pictureViewpager.adapter=pagerAdapter
        pictureTabLayout.setupWithViewPager(pictureViewpager,true)
    }

    override fun onError(message: String) {
        GlobalScope.launch(Dispatchers.Main) {
            downloadFab.setGone()
            errorLayout.setVisible()
            errorLayout.errorText.text=message
        }
    }

    override fun onDestroyView() {
        pictureViewpager.adapter=null
        super.onDestroyView()
    }
}

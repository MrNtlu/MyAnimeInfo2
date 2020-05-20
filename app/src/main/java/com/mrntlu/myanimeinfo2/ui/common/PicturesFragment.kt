package com.mrntlu.myanimeinfo2.ui.common

import android.Manifest
import android.content.pm.PackageManager
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.NavController
import androidx.navigation.Navigation
import com.mrntlu.myanimeinfo2.R
import com.mrntlu.myanimeinfo2.adapters.pageradapters.PicturesPagerAdapter
import com.mrntlu.myanimeinfo2.interfaces.CoroutinesErrorHandler
import com.mrntlu.myanimeinfo2.models.DataType
import com.mrntlu.myanimeinfo2.models.PictureBodyResponse
import com.mrntlu.myanimeinfo2.utils.setGone
import com.mrntlu.myanimeinfo2.utils.setVisible
import com.mrntlu.myanimeinfo2.viewmodels.CommonViewModel
import kotlinx.android.synthetic.main.cell_error.view.*
import kotlinx.android.synthetic.main.fragment_pictures.*
import kotlinx.android.synthetic.main.fragment_pictures.errorLayout
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
import androidx.core.content.ContextCompat
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.whenResumed
import com.mrntlu.myanimeinfo2.utils.showToast

class PicturesFragment : Fragment(), CoroutinesErrorHandler {

    private lateinit var commonViewModel: CommonViewModel
    private var malID by Delegates.notNull<Int>()
    private lateinit var navController: NavController
    private lateinit var dataType: DataType
    private lateinit var pictures:List<PictureBodyResponse>
    private lateinit var fragments: ArrayList<PictureItemFragment>
    private var PERMISSION_REQUEST_WRITE_STORAGE=1001

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
        commonViewModel= ViewModelProvider(this).get(CommonViewModel::class.java)

        if (isPermissionsNeeded(view)) requestPermission()
        setupObservers()
        setListeners()
    }

    private fun isPermissionsNeeded(view:View)=ContextCompat.checkSelfPermission(view.context, Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED

    private fun setListeners(){
        errorLayout.errorRefreshButton.setOnClickListener {
            downloadFab.setVisible()
            loadingLayout.setVisible()
            errorLayout.setGone()
            setupObservers()
        }

        downloadFab.setOnClickListener {
            if (::fragments.isInitialized){
                if (isPermissionsNeeded(it))
                    requestPermission()
                else {
                    if (fragments[pictureViewpager.currentItem].pictureImage!=null && fragments[pictureViewpager.currentItem].pictureImage.drawable!=null)
                        downloadImage(it, fragments[pictureViewpager.currentItem].pictureImage.drawable)
                }
            }
        }
    }

    private fun requestPermission()=requestPermissions(arrayOf(Manifest.permission.WRITE_EXTERNAL_STORAGE),PERMISSION_REQUEST_WRITE_STORAGE)


    @Suppress("DEPRECATION")
    private fun downloadImage(view:View, drawable: Drawable) {
        val bitmap = (drawable as BitmapDrawable).bitmap
        var fout: FileOutputStream? =null
        var imageMainDirectory:File?=null
        val root:File?

        try {
            root=File(view.context.getExternalFilesDir(null)!!.absolutePath)
            root.mkdirs()
            imageMainDirectory=File(root,"$malID${pictureViewpager.currentItem}.jpg")
            fout=FileOutputStream(imageMainDirectory)
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
                MediaStore.Images.Media.insertImage(view.context.contentResolver, bitmap, "$malID's ${pictureViewpager.currentItem}th Image", "Desc")
            }
            showToast(view.context,"Image saved to gallery. It might take a while to be shown.")
        }catch (e:Exception){
            e.printStackTrace()
        }
    }

    private fun setupObservers() {
        commonViewModel.getPicturesByID(dataType.toString().toLowerCase(Locale.ENGLISH),malID,this).observe(viewLifecycleOwner, Observer {
            loadingLayout.setGone()
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

    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<out String>, grantResults: IntArray) {
        when(requestCode){
            PERMISSION_REQUEST_WRITE_STORAGE->{
                if ((grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED)){
                    showToast(context,"Permission granted. Now you can save images.")
                }else{
                    showToast(context,"Permission denied. You can't save images.")
                }
            }
        }
    }

    override fun onError(message: String) {
        viewLifecycleOwner.lifecycleScope.launch {
            whenResumed {
                loadingLayout.setGone()
                downloadFab.setGone()
                errorLayout.setVisible()
                errorLayout.errorText.text = message
            }
        }
    }

    override fun onDestroyView() {
        pictureViewpager.adapter=null
        super.onDestroyView()
    }
}

package com.example.gstarena.profileScreen


import android.app.Activity
import android.content.Intent
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.graphics.ImageDecoder
import android.os.Build
import android.os.Bundle
import android.util.Base64
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.annotation.RequiresApi
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.example.gstarena.R
import com.example.gstarena.databinding.ProfileFragmentBinding
import java.io.ByteArrayOutputStream

private const val PICK_IMAGE = 1

class profileFragment : Fragment() {

    companion object {
        fun newInstance() = profileFragment()
    }

    private lateinit var binding: ProfileFragmentBinding

    private val viewModel: ProfileViewModel by lazy {
        val activity = requireNotNull(this.activity) {
            "You can only access the viewModel after onActivityCreated()"
        }
        ViewModelProvider(this, ProfileViewModel.Factory(activity))
            .get(ProfileViewModel::class.java)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        viewModel.image.observe(viewLifecycleOwner, Observer {
            it?.let {
                when(it) {
                    "" -> binding.profilePic.setImageResource(R.drawable.ic_launcher_background)
                    else -> binding.profilePic.setImageBitmap(decodeBase64(it))
                }
            }
        })
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = DataBindingUtil.inflate(inflater, R.layout.profile_fragment, container, false)
        binding.lifecycleOwner = viewLifecycleOwner
        binding.viewModel = viewModel
        binding.cameraRoll.setOnClickListener {
            val gallery = Intent()
            gallery.type = "image/*"
            gallery.action = Intent.ACTION_GET_CONTENT
            startActivityForResult(Intent.createChooser(gallery,"Select Image"),PICK_IMAGE)
        }
        return binding.root
    }

    @RequiresApi(Build.VERSION_CODES.P)
    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if(requestCode== PICK_IMAGE && resultCode==Activity.RESULT_OK && data!=null && data.data!=null) {
            val imageUri = data.data
            val source = ImageDecoder.createSource(requireActivity().contentResolver,imageUri!!)
            val bitmap = ImageDecoder.decodeBitmap(source)
            encodeBase64(bitmap)?.let { viewModel.saveImage(it) }
        }
    }

    private fun encodeBase64(image: Bitmap): String? {
        val stream = ByteArrayOutputStream()
        image.compress(Bitmap.CompressFormat.JPEG, 100, stream)
        val byteArray = stream.toByteArray()
        return Base64.encodeToString(byteArray, Base64.DEFAULT)
    }

    private fun decodeBase64(input: String?): Bitmap? {
        val decodeByte = Base64.decode(input, 0)
        return BitmapFactory
            .decodeByteArray(decodeByte, 0, decodeByte.size)
    }
}


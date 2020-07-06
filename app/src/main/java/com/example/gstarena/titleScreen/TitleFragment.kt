package com.example.gstarena.titleScreen

import android.app.Activity
import android.app.AlertDialog
import android.content.DialogInterface
import android.content.Intent
import android.os.Bundle
import android.view.*
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.gstarena.R
import com.example.gstarena.databinding.TitleFragmentBinding
import com.firebase.ui.auth.AuthUI
import com.firebase.ui.auth.IdpResponse
import kotlinx.coroutines.*
import java.lang.Exception


class TitleFragment : Fragment() {

    companion object {
        fun newInstance() = TitleFragment()
    }

    private val viewModelJob = SupervisorJob()
    private val viewModelScope = CoroutineScope(viewModelJob + Dispatchers.Main)
    private lateinit var binding : TitleFragmentBinding
    private val signIn = 123

    private val viewModel: TitleViewModel by lazy {
        val activity = requireNotNull(this.activity) {
            "You can only access the viewModel after onActivityCreated()"
        }
        ViewModelProvider(this, TitleViewModel.Factory(activity))
            .get(TitleViewModel::class.java)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        viewModel.user.observe(viewLifecycleOwner, Observer {user->
            if(user==null) {
                createSignInIntent()
            }
            else {
                viewModelScope.launch {
                    try {
                        viewModel.saveUserDetails()
                    } catch (e : Exception) {
                        var builder = AlertDialog.Builder(activity)
                        builder.setMessage("Error.If you don't have an account on SIES GST Arena,goto\n" +
                                "http://arena.siesgst.ac.in and try signing in again")
                        builder.setCancelable(true)
                        builder.setPositiveButton("Ok", DialogInterface.OnClickListener {
                                dialog: DialogInterface?, _: Int ->
                            dialog?.cancel()
                            signOut()
                        })
                        val alert = builder.create()
                        alert.show()
                    }
                }
            }
        })
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = DataBindingUtil.inflate<TitleFragmentBinding>(inflater,
            R.layout.title_fragment, container, false)
        binding.lifecycleOwner = this
        binding.viewModel = viewModel
        val adapter = TitleAdapter()
        binding.recyclerView.adapter = adapter
        binding.recyclerView.addItemDecoration(DividerItemDecoration(context,LinearLayoutManager.VERTICAL))
        setHasOptionsMenu(true)
        return binding.root
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        return inflater.inflate(R.menu.overflow_menu,menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean = when(item.itemId) {

        R.id.aboutFragment -> {
            this.findNavController().navigate(R.id.action_titleFragment_to_aboutFragment2)
            true
        }
        R.id.signOut -> {
            signOut()
            true
        }
        else -> super.onOptionsItemSelected(item)
    }

    private fun signOut() {
        AuthUI.getInstance().signOut(requireContext().applicationContext)
        viewModel.clearDetails()
    }

    private fun createSignInIntent() {

        val providers = arrayListOf(
            AuthUI.IdpConfig.EmailBuilder().build(),
            AuthUI.IdpConfig.GoogleBuilder().build())

        startActivityForResult(
            AuthUI.getInstance()
                .createSignInIntentBuilder()
                .setIsSmartLockEnabled(false)
                .setAvailableProviders(providers)
                .setLogo(R.drawable.ic_chefs_hat)
                .build(),
            signIn
        )
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == signIn) {
            val response = IdpResponse.fromResultIntent(data)
            if (resultCode != Activity.RESULT_OK) {
                Toast.makeText(requireContext().applicationContext,response?.error.toString(), Toast.LENGTH_LONG).show()
            }
        }
    }
}

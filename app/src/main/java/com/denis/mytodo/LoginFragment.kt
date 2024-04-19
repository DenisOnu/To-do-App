package com.denis.mytodo


import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.navigation.fragment.findNavController
import com.denis.mytodo.Preferences.isLoggedIn
import com.denis.mytodo.Preferences.setLoggedIn
import com.denis.mytodo.databinding.FragmentLoginBinding
import com.denis.mytodo.localDataBase.TodoDBHelper


class LoginFragment : BaseFragment() {


    lateinit var binding: FragmentLoginBinding
    lateinit var todoDBHelper: TodoDBHelper


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        if(context?.isLoggedIn() == true){
            findNavController().navigate(LoginFragmentDirections.navToHome())
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {


        return inflater.inflate(R.layout.fragment_login,container,false)
    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding= FragmentLoginBinding.bind(view)

        todoDBHelper= TodoDBHelper(requireContext())

        binding.newUserBtn.setOnClickListener {
            findNavController().navigate(LoginFragmentDirections.navToSignup())
        }
        binding.loginBtn.setOnClickListener {

            val username=binding.nameBox.text.toString().trim()
            val password=binding.passBox.text.toString().trim()

            if(username.isEmpty()||password.isEmpty()){
               Toast.makeText(requireContext(),"Username and password required",Toast.LENGTH_LONG).show()

            }else{
                login(username,password)
            }

        }

    }



    private fun login(name: String, password: String){

        if(todoDBHelper.loginUser(name,password)){
            requireContext().setLoggedIn(true)
            findNavController().navigate(LoginFragmentDirections.navToHome())
        }else{
            Toast.makeText(requireContext(),"Invalid Username or Password",Toast.LENGTH_LONG).show()
        }

    }




}
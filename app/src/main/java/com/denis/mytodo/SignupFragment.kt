package com.denis.mytodo


import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.navigation.fragment.findNavController
import com.denis.mytodo.Preferences.isLoggedIn
import com.denis.mytodo.dataClasess.UserModel
import com.denis.mytodo.databinding.FragmentLoginBinding
import com.denis.mytodo.databinding.FragmentSignupBinding
import com.denis.mytodo.localDataBase.DataHelper


class SignupFragment : BaseFragment() {


    lateinit var binding: FragmentSignupBinding
    lateinit var todoDBHelper: DataHelper



    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {


        return inflater.inflate(R.layout.fragment_signup,container,false)
    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding= FragmentSignupBinding.bind(view)
        todoDBHelper= DataHelper()


        binding.haveAccountBtn.setOnClickListener {
            findNavController().navigate(SignupFragmentDirections.navToLogin())
        }

        binding.signupBtn.setOnClickListener {

            val email=binding.emailBox.text.toString().trim()
            val username=binding.nameBox.text.toString().trim()
            val password=binding.passBox.text.toString().trim()

            if(!isValidEmail(email)){
                Toast.makeText(requireContext(),"Invalid Email Address",Toast.LENGTH_LONG).show()
                return@setOnClickListener
            }

            if(password.length<6){
                Toast.makeText(requireContext(),"Password must be at least 6 characters long",Toast.LENGTH_LONG).show()
                return@setOnClickListener
            }

            if(username.isEmpty()){
               Toast.makeText(requireContext(),"Username required",Toast.LENGTH_LONG).show()

            }else{
                signup(email,username,password)
            }

        }

    }



    private fun signup(email:String,name: String, password: String){

        val user=UserModel()
        user.email=email
        user.username=name
        user.password=password
        todoDBHelper.registerUser(user)
        Toast.makeText(requireContext(),"Registered Successfully",Toast.LENGTH_LONG).show()
        findNavController().navigate(SignupFragmentDirections.navToLogin())
    }


    fun isValidEmail(email: String): Boolean {
        val emailRegex = Regex("[a-zA-Z0-9._%+-]+@[a-zA-Z0-9.-]+\\.[a-zA-Z]{2,}")
        return emailRegex.matches(email)
    }


}
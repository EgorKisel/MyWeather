package com.geekbrains.myweather.lesson9

import android.Manifest
import android.annotation.SuppressLint
import android.content.ContentResolver
import android.content.pm.PackageManager
import android.os.Bundle
import android.provider.ContactsContract
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AlertDialog
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import com.geekbrains.myweather.databinding.FragmentWorkWithContentProviderBinding
import kotlinx.android.synthetic.main.fragment_work_with_content_provider.*


class WorkWithContentProviderFragment : Fragment() {
    private var _binding: FragmentWorkWithContentProviderBinding? = null
    private val binding: FragmentWorkWithContentProviderBinding
        get() {
            return _binding!!
        }

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentWorkWithContentProviderBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        checkPermission()
    }

    private fun checkPermission() {
        if (ContextCompat.checkSelfPermission(
                requireContext(),
                Manifest.permission.READ_CONTACTS
            ) == PackageManager.PERMISSION_GRANTED
        ) {
            getContacts()
        }else if (shouldShowRequestPermissionRationale(Manifest.permission.READ_CONTACTS)){
            explain()
        }else{
            mRequestPermission()
        }
    }

    private fun explain(){
        AlertDialog.Builder(requireContext())
            .setTitle("Доступ к контактам")
            .setMessage("Объяснение, контакы очень нужны приложению погода, без них приложение не сможет работать)")
            .setPositiveButton("Предоставить доступ"){_, _ ->
                mRequestPermission()
            }
            .setNegativeButton("Нет"){dialog, _ -> dialog.dismiss()}
            .create()
            .show()
    }

    private val REQUEST_CODE = 999
    private fun mRequestPermission() {
        requestPermissions(arrayOf(Manifest.permission.READ_CONTACTS), REQUEST_CODE)
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        if (requestCode==999){

            for (i in permissions.indices){
                if (permissions[i]==Manifest.permission.READ_CONTACTS&&grantResults[i]==PackageManager.PERMISSION_GRANTED){
                    getContacts()
                }else{
                    explain()
                }
            }
        }else{
            super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        }
    }

    @SuppressLint("Range")
    private fun getContacts() {
        var phoneNumber: String? = null

        //Связываемся с контактными данными и берем с них значения id контакта, имени контакта и его номера:
        val contentUri = ContactsContract.Contacts.CONTENT_URI
        val idContact = ContactsContract.Contacts._ID
        val displayName = ContactsContract.Contacts.DISPLAY_NAME
        val hasPhoneNumber = ContactsContract.Contacts.HAS_PHONE_NUMBER
        val phoneContentUri = ContactsContract.CommonDataKinds.Phone.CONTENT_URI
        val phoneContactId = ContactsContract.CommonDataKinds.Phone.CONTACT_ID
        val number = ContactsContract.CommonDataKinds.Phone.NUMBER
        val output = StringBuffer()
       val contentResolver: ContentResolver = requireContext().contentResolver
       val cursor = contentResolver.query(
           ContactsContract.Contacts.CONTENT_URI,
           null,
           null,
            null,
           ContactsContract.Contacts.DISPLAY_NAME + " ASC") // ASC - по возрастанию, или DESC - по уменьшению

        if (cursor!!.count > 0) {

            //Если значение имени и номера контакта больше 0 (то есть они существуют) выбираем
            //их значения в приложение привязываем с соответствующие поля "Имя" и "Номер":
            while (cursor.moveToNext()) {
                val contactId = cursor.getString(cursor.getColumnIndex(idContact))
                val name = cursor.getString(cursor.getColumnIndex(displayName))
                val hasPhoneNumber =
                    cursor.getString(cursor.getColumnIndex(hasPhoneNumber)).toInt()

                //Получаем имя:
                if (hasPhoneNumber > 0) {
                    output.append("\n Имя: $name")
                    val phoneCursor = contentResolver.query(
                        phoneContentUri, null,
                        "$phoneContactId = ?", arrayOf(contactId), null)

                    //и соответствующий ему номер:
                    while (phoneCursor!!.moveToNext()) {
                        phoneNumber = phoneCursor.getString(phoneCursor.getColumnIndex(number))
                        output.append("\n Телефон: $phoneNumber")
                    }
                }
                output.append("\n")
            }

            //Полученные данные отображаем с созданном элементе TextView:
            contacts!!.text = output
        }
    }

    companion object {
        @JvmStatic
        fun newInstance() = WorkWithContentProviderFragment()
    }
}
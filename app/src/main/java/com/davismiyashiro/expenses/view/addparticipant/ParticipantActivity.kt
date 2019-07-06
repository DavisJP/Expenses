/*
 * MIT License
 *
 * Copyright (c) 2019 Davis Miyashiro
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in all
 * copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
 * SOFTWARE.
 */
package com.davismiyashiro.expenses.view.addparticipant

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.support.design.widget.TextInputLayout
import android.view.Menu
import android.view.View
import android.view.WindowManager
import android.widget.EditText

import com.davismiyashiro.expenses.Injection
import com.davismiyashiro.expenses.R
import com.davismiyashiro.expenses.view.BaseCompatActivity
import com.davismiyashiro.expenses.datatypes.Participant
import com.davismiyashiro.expenses.datatypes.Tab

import timber.log.Timber

class ParticipantActivity : BaseCompatActivity(), ParticipantInterfaces.View, View.OnClickListener {
    private lateinit var mInputLayoutPartName: TextInputLayout
    private lateinit var mInputLayoutPartEmail: TextInputLayout
    private lateinit var mInputLayoutPartPhone: TextInputLayout
    private lateinit var mEditTextName: EditText
    private lateinit var mEditTextMail: EditText
    private lateinit var mEditTextPhone: EditText
    private var mTab: Tab? = null
    private var mParticipant: Participant? = null

    private var mPresenter: ParticipantInterfaces.UserActionsListener? = null

    private val isParticipantNew: Boolean
        get() = mParticipant == null

    override fun onCreate(savedInstanceState: Bundle?) {
        setContentView(R.layout.participant)
        super.onCreate(savedInstanceState)

        mInputLayoutPartName = findViewById(R.id.input_layout_participant_name)
        mEditTextName = findViewById(R.id.edtParticipantName)
        requestFocus(mEditTextName)

        mInputLayoutPartEmail = findViewById(R.id.input_layout_participant_email)
        mEditTextMail = findViewById(R.id.edtParticipantEmail)

        mInputLayoutPartPhone = findViewById(R.id.input_layout_participant_phone)
        mEditTextPhone = findViewById(R.id.edtParticipantPhone)

        findViewById<View>(R.id.fab_add_participant_details).setOnClickListener(this)

        mPresenter = ParticipantPresenterImpl(this, Injection.provideTabsRepository(applicationContext))

        mTab = intent.getParcelableExtra(TAB_PARAM)
        mParticipant = intent.getParcelableExtra(PART_PARAM)

        val ab = supportActionBar
        if (isParticipantNew) {
            ab?.setTitle(R.string.add_part_action_bar)
        } else {
            ab?.setTitle(R.string.edit_part_action_bar)
        }
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        menuInflater.inflate(R.menu.menu_add_participant, menu)
        return super.onCreateOptionsMenu(menu)
    }

    override fun onResume() {
        super.onResume()
        Timber.d("onResume")

        if (!isParticipantNew) {
            setParticipantName(mParticipant?.name.toString())
            setParticipantEmail(mParticipant?.email.toString())
            setParticipantPhone(mParticipant?.number.toString())
        }
    }

    override fun onSuccess() {

        val participant: Participant

        if (isParticipantNew) {
            participant = Participant(mEditTextName.text.toString(), mEditTextMail.text.toString(), mEditTextPhone.text.toString(), mTab?.groupId.toString())

            mPresenter?.addParticipant(participant)
        } else {
            participant = Participant.retrieveParticipant(mParticipant?.id.toString(), mEditTextName.text.toString(), mEditTextMail.text.toString(), mEditTextPhone.text.toString(), mTab?.groupId.toString())

            mPresenter?.updateParticipant(participant)
        }

        finish()
    }

    override fun setParticipantName(name: String) {
        mEditTextName.setText(name)
    }

    override fun setParticipantEmail(email: String) {
        mEditTextMail.setText(email)
    }

    override fun setParticipantPhone(phone: String) {
        mEditTextPhone.setText(phone)
    }

    override fun showNameError(value: Boolean) {
        if (value) {
            mInputLayoutPartName.error = getString(R.string.error_field_required)
            requestFocus(mEditTextName)
        }
        mInputLayoutPartName.isErrorEnabled = value
    }

    override fun showEmailError(value: Boolean) {
        if (value) {
            mInputLayoutPartEmail.error = getString(R.string.error_invalid_email)
            requestFocus(mEditTextMail)
        }
        mInputLayoutPartEmail.isErrorEnabled = value
    }

    override fun showPhoneError(value: Boolean) {
        if (value) {
            mInputLayoutPartPhone.error = getString(R.string.error_field_required)
            requestFocus(mEditTextPhone)
        } else {
            mInputLayoutPartPhone.error = null
            mInputLayoutPartPhone.isErrorEnabled = value
        }
    }

    override fun onClick(v: View) {
        mPresenter?.validateInputFields(mEditTextName.text.toString(), mEditTextMail.text.toString(), mEditTextPhone.text.toString())
    }

    private fun requestFocus(view: View) {
        if (view.requestFocus()) {
            window.setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_VISIBLE)
        }
    }

    companion object {

        private const val TAB_PARAM = "com.davismiyashiro.expenses.view.participant.Tab"
        private const val PART_PARAM = "com.davismiyashiro.expenses.view.participant.Participant"

        fun newInstance(context: Context, tab: Tab, participant: Participant?): Intent {
            val intent = Intent(context, ParticipantActivity::class.java)
            intent.putExtra(TAB_PARAM, tab)
            intent.putExtra(PART_PARAM, participant)
            return intent
        }
    }
}

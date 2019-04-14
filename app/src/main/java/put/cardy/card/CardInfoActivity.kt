package put.cardy.card

import android.content.Context
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.text.InputType
import android.transition.TransitionManager
import android.view.Gravity
import android.view.LayoutInflater
import android.widget.*
import kotlinx.android.synthetic.main.card_controllers.*
import kotlinx.android.synthetic.main.card_info.*
import org.joda.time.DateTime
import org.joda.time.format.DateTimeFormat
import put.cardy.R
import put.cardy.database.CardRepository
import put.cardy.database.GoalRepository
import put.cardy.model.Card
import put.cardy.model.Goal




class CardInfoActivity : AppCompatActivity() {

    var card: Card = Card()
    var cardGoal: Goal = Goal()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.card_info)

        disableInputs()
        getCardInfo()

        fab.setOnClickListener {
            openPopup()
        }

    }

    private fun openPopup() {
        val inflater: LayoutInflater = getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater

        val view = inflater.inflate(R.layout.popup, null)

        val popupWindow = PopupWindow(
            view,
            LinearLayout.LayoutParams.MATCH_PARENT,
            LinearLayout.LayoutParams.WRAP_CONTENT
        )
        popupWindow.elevation = 10.0F

        val date = view.findViewById<EditText>(R.id.date)
        date.inputType = InputType.TYPE_NULL
        date.setText(getActualDate())

        val addButton = view.findViewById<Button>(R.id.button_popup)

        TransitionManager.beginDelayedTransition(root_layout)
        popupWindow.showAtLocation(
            root_layout,
            Gravity.BOTTOM,
            0,
            0
        )

    }

    private fun getActualDate(): String? {
        val now = DateTime.now()
        val fmt = DateTimeFormat.forPattern("d MMMM, yyyy")
        val str = now.toString(fmt)
        return str
    }

    private fun disableInputs() {
        cardNumber.inputType = InputType.TYPE_NULL
        bankName.inputType = InputType.TYPE_NULL
        timeSpinner.isEnabled = false
        typeSpinner.isEnabled = false
        goal.inputType = InputType.TYPE_NULL
        actualGoal.inputType = InputType.TYPE_NULL


    }

    private fun getCardInfo() {
        val intent = intent
        val id = intent.getLongExtra("id", 0)
        card = CardRepository(this).findById(id)
        cardGoal = GoalRepository(this).findByCardId(card.id)

        cardNumber.setText(card.number)
        bankName.setText(card.bankName)
        typeSpinner.setSelection(getIndex(typeSpinner, cardGoal.type!!.name))
        timeSpinner.setSelection(getIndex(timeSpinner, cardGoal.period.toString()))
        goal.setText(cardGoal.goal.toString())
        actualGoal.setText(cardGoal.actualGoal.toString())
    }

    private fun getIndex(spinner: Spinner, myString: String): Int {
        for (i in 0 until spinner.count) {
            if (spinner.getItemAtPosition(i).toString().equals(myString, ignoreCase = true)) {
                return i
            }
        }

        return 0
    }

}
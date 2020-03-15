package com.example.splitthebillv2

import android.annotation.SuppressLint
import android.app.ActionBar
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.appcompat.app.AppCompatActivity
import kotlinx.android.synthetic.main.activity_scrolling2.*
import kotlinx.android.synthetic.main.content_scrolling2.*


fun setListViewHeightBasedOnChildren(listView: ListView ) {
    val listAdapter: ListAdapter? = listView.getAdapter()
    if (listAdapter == null)
        return
    val desiredWidth: Int = View.MeasureSpec.makeMeasureSpec(listView.getWidth(), View.MeasureSpec.UNSPECIFIED)
    var totalHeight: Int = 0
    var view : View? = null
    for (i in 0 until listAdapter.getCount()) {
        view = listAdapter.getView(i, view, listView);
        if (i == 0) view.setLayoutParams(ViewGroup.LayoutParams(desiredWidth, ActionBar.LayoutParams.WRAP_CONTENT));

        view.measure(desiredWidth, View.MeasureSpec.UNSPECIFIED);
        totalHeight += 140
    }

    val params = listView.getLayoutParams();

    params.height = totalHeight //+ ((listView.getDividerHeight()) * (listAdapter.getCount() - 1));

    listView.setLayoutParams(params);
    listView.requestLayout();
}

@Suppress("RECEIVER_NULLABILITY_MISMATCH_BASED_ON_JAVA_ANNOTATIONS")
class ScrollingActivity2 : AppCompatActivity(), AdapterView.OnItemSelectedListener  {

    override fun onItemSelected(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {}
    override fun onNothingSelected(parent: AdapterView<*>?) {}

    @SuppressLint("SetTextI18n")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_scrolling2)
        setSupportActionBar(toolbar)

        val txt: String = intent.getStringExtra(EXTRA_MESSAGE).toString()
        val list1: ArrayList<String> = txt.split("\n") as ArrayList<String>
        val list: MutableList<String> = emptyArray<String>().toMutableList()
        val n = list1.size
        for (i in 1 until n) {
            list.add(i - 1, list1[i])
        }


        val spinner = this.spinnerWhoPaid
        spinner.setOnItemSelectedListener(this)
        val array_adapter = ArrayAdapter(this, android.R.layout.simple_spinner_item, list)
        array_adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        spinner.setAdapter(array_adapter)


        val adapter2 = ArrayAdapter(this, android.R.layout.simple_list_item_multiple_choice, list)
        listViewDebtors.adapter = adapter2
        listViewDebtors.choiceMode = ListView.CHOICE_MODE_MULTIPLE
        setListViewHeightBasedOnChildren(listViewDebtors)

        val zero : Double = 0.0
        var debt : MutableList<Double> = MutableList(n - 1, {_ -> zero})

        Sum.addTextChangedListener(object : TextWatcher {
            override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {}

            override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {}

            override fun afterTextChanged(p0: Editable?) {
                val s = Sum.text.toString()
                var k: Int = 0
                for (i in 0 until s.length) {
                    if (!('0' <= s[i] && s[i] <= '9' || s[i] == '.' || s[i] == '-')) {
                        Sum.setText("0")
                        return
                    }
                    if (s[i] == '.')
                        k += 1
                    if (k > 1) {
                        Sum.setText("0")
                        return
                    }
                }
            }
        })

        buttonAdd.setOnClickListener{
            val sum = Sum.text.toString().toDouble()
            debt[spinnerWhoPaid.selectedItemPosition] -= sum
            var number_of_debtors : Int = 0

            var s: String = "${list[spinnerWhoPaid.selectedItemPosition]} paid ${sum} for"

            with(listViewDebtors.checkedItemPositions) {
                for (i in 0 until size()) {
                    val key = keyAt(i)
                    val value = this[key]
                    if (value){
                        number_of_debtors  += 1
                        s = "${s} ${list[key]}"
                    }
                }
            }
            history.text = "${history.text}${s}\n"
            with(listViewDebtors.checkedItemPositions) {
                for (i in 0 until size()) {
                    val key = keyAt(i)
                    val value = this[key]
                    if (value){
                        debt[key] += sum / number_of_debtors
                    }
                }
            }

            var bank : Int = -1
            val eps: Double = 0.000001
            for (i in 0 until n - 1) {
                if (debt[i] < -eps) {
                    bank = i
                    break
                }
            }
            results.text = "Results:\n"
            if (bank != -1) {
                for (i in 0 until n - 1) {
                    if (i == bank)
                        continue
                    val a = (debt[i] * 100).toInt() / 100.0
                    if (debt[i] > eps)
                        results.text =
                            "${results.text}${list[i]} should give ${a} to ${list[bank]}\n"
                    if (debt[i] < -eps)
                        results.text =
                            "${results.text}${list[bank]} should give ${-a} to ${list[i]}\n"
                }
            }
        }
    }
}

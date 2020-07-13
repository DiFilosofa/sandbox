package com.difilosofa.sandbox.livedata_list

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.SearchView
import androidx.lifecycle.MutableLiveData
import androidx.recyclerview.widget.LinearLayoutManager
import com.difilosofa.sandbox.R
import kotlinx.android.synthetic.main.activity_livedata_list.*
import kotlinx.coroutines.*

/*
    This activity is the demo of
    how a RecyclerView's adapter can update the data content
    without having to reload the whole View Holder using LiveData as an Item in the adapter
 */

class LiveDataListActivity : AppCompatActivity(R.layout.activity_livedata_list) {
    var queryTextChangedJob: Job? = null
    private val items = mutableListOf(
        MutableLiveData(
            QueryItem(
                "https://images.pexels.com/photos/1083822/pexels-photo-1083822.jpeg?auto=compress&cs=tinysrgb&dpr=2&h=750&w=1260",
                "Success largely boils down to a simple distinction."
            )
        ),
        MutableLiveData(
            QueryItem(
                "https://images.pexels.com/photos/1086178/pexels-photo-1086178.jpeg?auto=compress&cs=tinysrgb&dpr=3&h=750&w=1260",
                "It’s glaringly obvious once you see it,"
            )
        ),
        MutableLiveData(
            QueryItem(
                "https://images.pexels.com/photos/545042/pexels-photo-545042.jpeg?auto=compress&cs=tinysrgb&dpr=2&h=750&w=1260",
                "but also easy to find ingenious ways of ignoring it:"
            )
        ),
        MutableLiveData(
            QueryItem(
                "https://images.pexels.com/photos/1098520/pexels-photo-1098520.jpeg?auto=compress&cs=tinysrgb&dpr=2&h=750&w=1260",
                "do the real thing and stop doing fake alternatives."
            )
        ),
        MutableLiveData(
            QueryItem(
                "https://images.pexels.com/photos/1172849/pexels-photo-1172849.jpeg?auto=compress&cs=tinysrgb&dpr=2&h=750&w=1260",
                "Consider one person who wrote to me saying she turned down a job working in French."
            )
        ),
        MutableLiveData(
            QueryItem(
                "https://images.pexels.com/photos/736230/pexels-photo-736230.jpeg?auto=compress&cs=tinysrgb&dpr=2&h=750&w=1260",
                "She didn’t feel her French was good enough yet."
            )
        ),
        MutableLiveData(
            QueryItem(
                "https://images.pexels.com/photos/1179863/pexels-photo-1179863.jpeg?auto=compress&cs=tinysrgb&dpr=2&h=750&w=1260",
                "So instead, she planned to listen to podcasts at home every day until she was ready."
            )
        ),
        MutableLiveData(
            QueryItem(
                "https://images.pexels.com/photos/934011/pexels-photo-934011.jpeg?auto=compress&cs=tinysrgb&dpr=2&h=750&w=1260",
                "You know what would have helped her get good at French?"
            )
        ),
        MutableLiveData(
            QueryItem(
                "https://images.pexels.com/photos/70741/cereals-field-ripe-poppy-70741.jpeg?auto=compress&cs=tinysrgb&dpr=2&h=750&w=1260",
                "Working at the job in French."
            )
        ),
        MutableLiveData(
            QueryItem(
                "https://images.pexels.com/photos/931177/pexels-photo-931177.jpeg?auto=compress&cs=tinysrgb&dpr=2&h=750&w=1260",
                "Working at a job in the language she wanted to speak was the real thing for her."
            )
        ),
        MutableLiveData(
            QueryItem(
                "https://images.pexels.com/photos/3732659/pexels-photo-3732659.jpeg?auto=compress&cs=tinysrgb&dpr=2&h=750&w=1260",
                "Listening to podcasts at home to prepare was the fake alternative she chose instead."
            )
        ),
        MutableLiveData(
            QueryItem(
                "https://images.pexels.com/photos/1408221/pexels-photo-1408221.jpeg?auto=compress&cs=tinysrgb&dpr=2&h=750&w=1260",
                "Or consider another person I spoke with who wanted to get better at writing music."
            )
        ),
        MutableLiveData(
            QueryItem(
                "https://images.pexels.com/photos/3746214/pexels-photo-3746214.jpeg?auto=compress&cs=tinysrgb&dpr=2&h=750&w=1260",
                "He had come up with a complex analysis project."
            )
        ),
        MutableLiveData(
            QueryItem(
                "https://images.pexels.com/photos/617967/pexels-photo-617967.jpeg?auto=compress&cs=tinysrgb&dpr=2&h=750&w=1260",
                "He was going to do a deep dive into past hits,"
            )
        ),
        MutableLiveData(
            QueryItem(
                "https://images.pexels.com/photos/60909/rose-yellow-flower-petals-60909.jpeg?auto=compress&cs=tinysrgb&dpr=2&h=750&w=1260",
                "figuring out what made them great. In all this complexity he ignored the obvious,"
            )
        ),
        MutableLiveData(
            QueryItem(
                "https://images.pexels.com/photos/709847/pexels-photo-709847.jpeg?auto=compress&cs=tinysrgb&dpr=2&h=750&w=1260",
                "real thing he should be doing: writing more songs."
            )
        ),
        MutableLiveData(
            QueryItem(
                "https://images.pexels.com/photos/3294254/pexels-photo-3294254.jpeg?auto=compress&cs=tinysrgb&dpr=2&h=750&w=1260",
                "When I asked how many he had written so far, he said it was just three."
            )
        ),
        MutableLiveData(
            QueryItem(
                "https://images.pexels.com/photos/1166869/pexels-photo-1166869.jpeg?auto=compress&cs=tinysrgb&dpr=2&h=750&w=1260",
                "Business owners who spend more time printing business cards than finding clients."
            )
        )
    )

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        initList()
        initSearchView()
    }

    private fun initSearchView() {
        vSearch.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(query: String?): Boolean {
                // DO NOTHING
                return true
            }

            override fun onQueryTextChange(newText: String?): Boolean {
                queryTextChangedJob?.cancel()

                queryTextChangedJob = CoroutineScope(Dispatchers.Main).launch {
                    delay(500L)
                    items.forEach {
                        if (newText != null && newText.isNotBlank()) {
                            if (it.value?.text?.contains(newText) == true) {
                                it.value =
                                    QueryItem(
                                        it.value!!.imgUrl,
                                        it.value!!.text.replace(newText, "<b>$newText</b>", true)
                                    )
                            }
                        }
                    }
                }
                return true
            }

        })
    }

    private fun initList() {
        rcvText.layoutManager = LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false)
        rcvText.adapter =
            LiveDataListAdapter(items)
        rcvText.scheduleLayoutAnimation()
    }
}
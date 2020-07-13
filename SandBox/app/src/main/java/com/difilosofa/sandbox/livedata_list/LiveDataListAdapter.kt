package com.difilosofa.sandbox.livedata_list

import android.text.Html
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.animation.AnimationUtils
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.difilosofa.sandbox.R
import kotlinx.android.synthetic.main.item_livedata_adapter.view.*
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch


class LiveDataListAdapter(
    private val items: List<MutableLiveData<QueryItem>>
) : RecyclerView.Adapter<LiveDataListAdapter.QueryItemVH>() {

    inner class QueryItemVH(parent: ViewGroup) : RecyclerView.ViewHolder(
        LayoutInflater.from(parent.context).inflate(
            R.layout.item_livedata_adapter,
            parent,
            false
        )
    ) {
        fun bindData(data: MutableLiveData<QueryItem>) {
            itemView.tvText.setText(Html.fromHtml(data.value?.text))
            Glide.with(itemView).load(R.drawable.gifplaceholder).into(itemView.ivIcon)
            CoroutineScope(Dispatchers.Main).launch {
                delay(1000L)
                Glide.with(itemView)
                    .load(data.value?.imgUrl)
                    .thumbnail(Glide.with(itemView).load(R.drawable.gifplaceholder))
                    .fitCenter()
                    .into(itemView.ivIcon)
            }
            data.removeObservers(itemView.context as LifecycleOwner)
            data.observe(itemView.context as LifecycleOwner, Observer {
                itemView.tvText.setText(Html.fromHtml(data.value?.text))
            })
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int) = QueryItemVH(parent)
    override fun getItemCount(): Int = items.size

    override fun onBindViewHolder(holder: QueryItemVH, position: Int) {
        setFadeAnimation(holder.itemView)
        holder.bindData(items[position])
    }

    override fun onViewDetachedFromWindow(holder: QueryItemVH) {
        super.onViewDetachedFromWindow(holder)
        val anim =
            AnimationUtils.loadAnimation(holder.itemView.context, android.R.anim.slide_out_right)
        anim.duration = 650
        holder.itemView.startAnimation(anim)
    }

    fun setFadeAnimation(view: View) {
        val anim = AnimationUtils.loadAnimation(view.context, android.R.anim.slide_in_left)
        anim.duration = 650
        view.startAnimation(anim)
    }
}
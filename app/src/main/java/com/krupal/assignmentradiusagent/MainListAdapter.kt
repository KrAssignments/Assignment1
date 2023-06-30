package com.krupal.assignmentradiusagent

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.RecyclerView
import com.krupal.assignmentradiusagent.database.model.FacilityTable
import com.krupal.assignmentradiusagent.databinding.ListItemBinding

class MainListAdapter constructor(
    private val context: Context,
    private val list: ArrayList<FacilityTable> = arrayListOf()
) : RecyclerView.Adapter<MainListAdapter.ViewHolder>() {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val inflater = LayoutInflater.from(context)
        val binding: ListItemBinding =
            DataBindingUtil.inflate(inflater, R.layout.list_item, parent, false)
        return ViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(list[position])
    }

    override fun getItemCount(): Int = list.size

    open class ViewHolder(private val view: ListItemBinding) : RecyclerView.ViewHolder(view.root) {
        fun bind(item: FacilityTable) {
            view.text.text = item.facilityName
            val icon = when (item.optionName) {
                "apartment" -> R.drawable.apartment
                "boat" -> R.drawable.boat
                "condo" -> R.drawable.condo
                "garage" -> R.drawable.garage
                "garden" -> R.drawable.garden
                "land" -> R.drawable.land
                "no_room" -> R.drawable.no_room
                "rooms" -> R.drawable.rooms
                "swimming" -> R.drawable.swimming
                else -> R.drawable.no_room
            }
            view.image.setImageResource(icon)
        }
    }

    fun updateData(list: List<FacilityTable>){
        this.list.clear()
        this.list.addAll(list)
        notifyDataSetChanged()
    }
}
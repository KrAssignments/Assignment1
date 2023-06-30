package com.krupal.assignmentradiusagent

import android.content.Context
import android.graphics.Color
import android.graphics.drawable.Drawable
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.core.view.isVisible
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.RecyclerView
import com.krupal.app_domain.entity.ExclusionEntity
import com.krupal.app_domain.entity.FacilityEntity
import com.krupal.assignmentradiusagent.databinding.ListItemBinding

class MainListAdapter constructor(
    private val context: Context,
    private val list: ArrayList<Pair<Boolean, FacilityEntity>> = arrayListOf()
) : RecyclerView.Adapter<RecyclerView.ViewHolder>() {
    private val selectionMap: HashMap<Int, Int> = HashMap()
    private var exclisionList: ArrayList<ExclusionEntity> = arrayListOf()
    private val selectionDrawable: Drawable? =
        ContextCompat.getDrawable(context, R.drawable.baseline_check_circle_outline_24)
    private val isBlocked = HashMap<Int, Int>()
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        return if (viewType == 1) {
            val inflater = LayoutInflater.from(context)
            val binding: ListItemBinding =
                DataBindingUtil.inflate(inflater, R.layout.list_item, parent, false)
            TitleHolder(binding)
        } else {
            val inflater = LayoutInflater.from(context)
            val binding: ListItemBinding =
                DataBindingUtil.inflate(inflater, R.layout.list_item, parent, false)
            ViewHolder(binding)
        }
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        if (getItemViewType(position) == 1) {
            (holder as TitleHolder).bind(list[position].second)
        } else {
            (holder as ViewHolder).bind(list[position].second)
        }
    }

    override fun getItemCount(): Int = list.size

    fun getItem(index: Int) = list[index]

    override fun getItemViewType(position: Int): Int {
        return if (list[position].first) 1 else 2
    }

    inner class ViewHolder(private val view: ListItemBinding) : RecyclerView.ViewHolder(view.root),
        View.OnClickListener {
        init {
            view.content.setOnClickListener(this)
        }

        fun bind(item: FacilityEntity) {
            val selected = item.facilityId == 1 || selectionMap.containsKey(item.facilityId)
            view.content.setCardBackgroundColor(
                if (selected || selectionMap.containsKey(item.facilityId - 1) || isBlocked.containsKey(
                        item.facilityId
                    ) && isBlocked.getValue(item.facilityId) == item.optionId
                ) Color.WHITE else Color.LTGRAY
            )
            view.content.isClickable = selected

            view.text.text = item.optionName
            val icon = when (item.optionIconName) {
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
            view.text.setCompoundDrawablesWithIntrinsicBounds(
                null,
                null,
                if (selectionMap.containsKey(item.facilityId) && selectionMap.getValue(item.facilityId) == item.optionId) selectionDrawable else null,
                null
            )
        }

        override fun onClick(v: View?) {
            val fId = getItem(adapterPosition).second.facilityId
            val oId = getItem(adapterPosition).second.optionId

            selectionMap[fId] = oId
            isBlocked.clear()
            exclisionList.firstOrNull { it.facilityId == fId && it.optionId == oId }?.let {
                isBlocked.put(it.exclusionFacilityId, it.exclusionOptionId)
            }

            notifyDataSetChanged()
        }
    }

    inner class TitleHolder(private val view: ListItemBinding) :
        RecyclerView.ViewHolder(view.root) {
        init {
            view.content.setCardBackgroundColor(Color.YELLOW)
            view.image.isVisible = false
        }

        fun bind(item: FacilityEntity) {
            val selected = item.facilityId == 1 || selectionMap.containsKey(item.facilityId - 1)
            view.content.setCardBackgroundColor(if (selected) Color.YELLOW else Color.GRAY)
            view.text.text = "Select ${item.facilityName}"
        }
    }

    fun updateData(list: List<FacilityEntity>, exclusionList: List<ExclusionEntity>) {
        this.list.clear()
        var lastFc: FacilityEntity? = null
        list.forEach { l1 ->
            lastFc?.let {
                if (it.facilityId != l1.facilityId) {
                    this.list.add(Pair(true, l1))
                }
                this.list.add(Pair(false, l1))
                lastFc = l1
            } ?: let {
                lastFc = l1
                this.list.add(Pair(true, l1))
                this.list.add(Pair(false, l1))
            }
        }
        this.exclisionList.clear()
        this.exclisionList.addAll(exclusionList)
        notifyDataSetChanged()
    }

    fun resetSelection() {
        selectionMap.clear()
        isBlocked.clear()
        notifyDataSetChanged()
    }
}
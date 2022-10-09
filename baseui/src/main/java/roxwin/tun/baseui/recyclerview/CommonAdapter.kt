package roxwin.tun.baseui.recyclerview

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.EdgeEffect
import androidx.recyclerview.widget.RecyclerView
import roxwin.tun.baseui.dialog.setOnSingleClickListener

abstract class CommonAdapter<T>(var _listItem: MutableList<T> = mutableListOf()) :
    RecyclerView.Adapter<CommonViewHolder<T>>() {

    open var itemClickListener: ((item: T, position: Int) -> Unit)? = null

    abstract val itemLayoutRes: Int

    open fun onRefresh(listItem: List<T>) {
        _listItem.clear()
        _listItem.addAll(listItem)
        notifyDataSetChanged()
    }
    open fun onRefresh(listItem: Array<T>) {
        _listItem.clear()
        _listItem.addAll(listItem)
        notifyDataSetChanged()
    }

    val onScrollListener = object : RecyclerView.OnScrollListener() {
        override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
            super.onScrolled(recyclerView, dx, dy)
            recyclerView.forEachVisibleHolder { holder: CommonViewHolder<Any> ->
                holder.rotation
                    .setStartVelocity(holder.currentVelocity - dx * SCROLL_ROTATION_MAGNITUDE)
                    .start()
            }
        }
    }

    val edgeEffectFactory = object : RecyclerView.EdgeEffectFactory() {
        override fun createEdgeEffect(view: RecyclerView, direction: Int): EdgeEffect {
            return object : EdgeEffect(view.context) {
                override fun onPull(deltaDistance: Float) {
                    super.onPull(deltaDistance)
                    handlePull(deltaDistance)
                }

                override fun onPull(deltaDistance: Float, displacement: Float) {
                    super.onPull(deltaDistance, displacement)
                    handlePull(deltaDistance)
                }

                override fun onAbsorb(velocity: Int) {
                    super.onAbsorb(velocity)
                    view.forEachVisibleHolder { holder: CommonViewHolder<Any> ->
                        val sign = if (direction == DIRECTION_BOTTOM) -1 else 1

                        val translationVelocity = sign * velocity * 2 * FLING_TRANSLATION_MAGNITUDE
                        holder.translationY
                            .setStartVelocity(translationVelocity)
                            .start()
                    }
                }

                override fun onRelease() {
                    super.onRelease()
                    view.forEachVisibleHolder { holder: CommonViewHolder<Any> ->
                        holder.rotation.start()
                        holder.translationY.start()
                    }
                }

                private fun handlePull(deltaDistance: Float) {
                    val sign = if (direction == DIRECTION_BOTTOM) -1 else 1
                    val rotationDelta = sign * deltaDistance * OVERSCROLL_ROTATION_MAGNITUDE
                    val translationYDelta =
                        sign * view.height * deltaDistance * OVERSCROLL_TRANSLATION_MAGNITUDE
                    view.forEachVisibleHolder { holder: CommonViewHolder<Any> ->
                        holder.rotation.cancel()
                        holder.translationY.cancel()
//                            it.itemView.rotation += rotationDelta
                        holder.itemView.translationY += translationYDelta

                    }
                }
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CommonViewHolder<T> {
        val view = LayoutInflater.from(parent.context).inflate(itemLayoutRes, parent, false)
        return object : CommonViewHolder<T>(view) {
            init {
            }
            override fun onBind(item: T) {
                onBindItem(item, itemView)
                itemView.setOnSingleClickListener {
                    itemClickListener?.let {
                        it(item, adapterPosition)
                    }
                }
            }

        }
    }

    override fun getItemCount(): Int = _listItem.size

    override fun onBindViewHolder(holder: CommonViewHolder<T>, position: Int) {
        holder.onBind(_listItem[position])
    }

    abstract fun onBindItem(item: T, itemView: View)

}

/** The magnitude of rotation while the list is scrolled. */
const val SCROLL_ROTATION_MAGNITUDE = 0.25f

/** The magnitude of rotation while the list is over-scrolled. */
const val OVERSCROLL_ROTATION_MAGNITUDE = -10

/** The magnitude of translation distance while the list is over-scrolled. */
const val OVERSCROLL_TRANSLATION_MAGNITUDE = 0.2f

/** The magnitude of translation distance when the list reaches the edge on fling. */
const val FLING_TRANSLATION_MAGNITUDE = 0.5f
inline fun <reified T : RecyclerView.ViewHolder> RecyclerView.forEachVisibleHolder(action: (T) -> Unit) {
    for (i in 0 until childCount) {
        action(getChildViewHolder(getChildAt(i)) as T)
    }
}
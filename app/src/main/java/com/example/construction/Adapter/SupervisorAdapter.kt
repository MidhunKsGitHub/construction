package com.example.construction.Adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.CheckBox
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.construction.Model.Supervisor.Supervisor
import com.example.construction.R
import com.example.construction.Utils.MidhunUtils
import java.text.SimpleDateFormat
import java.util.Date


class SupervisorAdapter(val context: Context, val supervisorList: List<Supervisor>) :
    RecyclerView.Adapter<SupervisorAdapter.SupervisorViewHolder>() {

    val attendList = ArrayList<String>()
    val markAllList = ArrayList<String>()
     var checkAll:Boolean=false


    class SupervisorViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        var name: TextView
        var sub: TextView
        var checkBox: CheckBox

        init {
            name = itemView.findViewById(R.id.name)
            sub = itemView.findViewById(R.id.sub)
            checkBox = itemView.findViewById(R.id.check)
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): SupervisorViewHolder {
        val view =
            LayoutInflater.from(context).inflate(R.layout.custom_supervisor_item, parent, false)
        return SupervisorViewHolder(view)
    }

    override fun getItemCount(): Int {
        return supervisorList.size
    }

    override fun onBindViewHolder(holder: SupervisorViewHolder, position: Int) {
        val supervisor = supervisorList[position]

        val sdf = SimpleDateFormat("yyyy-MM-dd")
        var currentDate = sdf.format(Date())
        var isAdded=supervisor.employee_id+currentDate
        if(MidhunUtils.localDataCtx(context,"mark",isAdded).equals("added")){
            holder.checkBox.visibility=View.GONE
            holder.sub.setText("Attendance added")
        }
        else{
            holder.checkBox.visibility=View.VISIBLE
            holder.sub.setText("Attendance not added")

        }

        holder.name.text = supervisor.name
        if(checkAll==true) {
            holder.checkBox.isChecked = true
        }
        else{
            holder.checkBox.isChecked=false
        }
        markAllList.add(supervisor.employee_id)

        holder.checkBox.setOnClickListener {
            if (holder.checkBox.isChecked) {
                attendList.add(element = supervisor.employee_id)
            } else {

                if (attendList.contains(element = supervisor.employee_id)) {
                    attendList.remove(element = supervisor.employee_id)
                }


            }
        }

    }

    fun attend(): ArrayList<String> {

        return attendList
    }

    fun markAll(): ArrayList<String> {

        return markAllList
    }

    fun checkBoxMarkAll(){
        checkAll=true
        notifyDataSetChanged()
    }

    fun umCheckBoxMarkAll(){
        checkAll=false
        markAllList.clear()
        notifyDataSetChanged()
    }


}
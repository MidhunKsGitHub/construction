package com.example.construction.Adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.construction.Model.SuperVisorReport.Reportsupervisor
import com.example.construction.R

class SuperVisorReportAdapter(val context: Context, val supervVisorReportList: ArrayList<Reportsupervisor>) :
    RecyclerView.Adapter<SuperVisorReportAdapter.ReportViewHolder>() {

    class ReportViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
         var date: TextView
         var time: TextView
         var name: TextView

        init {
            date = itemView.findViewById(R.id.date_txt)
            time = itemView.findViewById(R.id.checkin_time)
            name = itemView.findViewById(R.id.name_txt)

        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ReportViewHolder {
        var view =
            LayoutInflater.from(context).inflate(R.layout.custom_supervisor_report_item, parent, false)
        return ReportViewHolder(view)
    }

    override fun getItemCount(): Int {
        return supervVisorReportList.size
    }

    override fun onBindViewHolder(holder: ReportViewHolder, position: Int) {
        var superVisorResport = supervVisorReportList[position]
        holder.name.visibility=View.VISIBLE
        holder.name.setText(superVisorResport.name)
        holder.date.setText(superVisorResport.date)
        holder.time.setText(superVisorResport.time)

    }
}
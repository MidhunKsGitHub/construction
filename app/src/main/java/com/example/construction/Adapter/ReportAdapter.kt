package com.example.construction.Adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.construction.Model.Report.PageData
import com.example.construction.Model.Report.Report
import com.example.construction.R
import com.example.construction.Utils.MidhunUtils

class ReportAdapter(val context: Context, val reportList: List<PageData>) :
    RecyclerView.Adapter<ReportAdapter.ReportViewHolder>() {

    class ReportViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
         var date: TextView
         var time: TextView

        init {
            date = itemView.findViewById(R.id.date_txt)
            time = itemView.findViewById(R.id.checkin_time)

        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ReportViewHolder {
        var view =
            LayoutInflater.from(context).inflate(R.layout.custom_emp_report_item, parent, false)
        return ReportViewHolder(view)
    }

    override fun getItemCount(): Int {
        return reportList.size
    }

    override fun onBindViewHolder(holder: ReportViewHolder, position: Int) {
        var report = reportList[position]
        holder.date.setText(report.checkin_date)
        holder.time.setText(report.checkin_time)

    }
}
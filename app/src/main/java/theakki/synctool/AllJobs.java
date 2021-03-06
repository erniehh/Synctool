package theakki.synctool;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.ContextMenu;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ListView;

import java.util.ArrayList;

import theakki.synctool.Job.JobHandler;
import theakki.synctool.Job.JobInfo;
import theakki.synctool.View.JobInfoViewAdapter;

import static junit.framework.Assert.*;

/**
 * Created by theakki on 06.04.18.
 */

public class AllJobs extends AppCompatActivity
{
    private ListView _listAllJobs;
    private Button _btnStartAll;

    @Override
    public void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        // Get the view from new_activity.xml
        setContentView(R.layout.activity_all_jobs);

        // List view
        _listAllJobs = findViewById(R.id.lv_jobs);
        assertNotNull("Listview 'jobs' not found", _listAllJobs);
        registerForContextMenu(_listAllJobs);
        loadData();

        // Start All Button
        _btnStartAll = findViewById(R.id.btn_StartAll);
        assertNotNull("Button 'Start' not found", _btnStartAll);
        _btnStartAll.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                onButtonAllStartClick();
            }
        });
    }

    private void loadData()
    {
        ArrayList<JobInfo> jobInfo = JobHandler.getInstance().getJobInfo();
        JobInfoViewAdapter viewAdapter = new JobInfoViewAdapter(this, R.layout.listview_jobdetail, jobInfo, true);
        _listAllJobs.setAdapter(viewAdapter);
    }


    private class ContextId
    {
        public static final int EDIT = 1;
        public static final int DELETE = 2;
        public static final int RUN = 3;
    }

    @Override
    public void onResume()
    {
        super.onResume();

        JobHandler.getInstance().resetStatusWhenFinished();
    }


    @Override
    public void onCreateContextMenu(ContextMenu menu, View v, ContextMenu.ContextMenuInfo menuInfo)
    {
        super.onCreateContextMenu(menu, v, menuInfo);

        if(v == _listAllJobs)
        {
            menu.setHeaderTitle(R.string.ContextM_SelectAction);
            menu.add(Menu.NONE, ContextId.RUN, Menu.NONE, R.string.ContextM_Run);
            menu.add(Menu.NONE, ContextId.EDIT, Menu.NONE, R.string.ContextM_Edit);
            menu.add(Menu.NONE, ContextId.DELETE, Menu.NONE, R.string.ContextM_Delete);
        }
    }

    @Override
    public boolean onContextItemSelected(MenuItem item)
    {
        AdapterView.AdapterContextMenuInfo info = (AdapterView.AdapterContextMenuInfo) item.getMenuInfo();
        switch (item.getItemId())
        {
            case ContextId.EDIT:
                onContextEditClick(info.id);
                return true;

            case ContextId.DELETE:
                onContextDeleteClick(info.id);
                return true;

            case ContextId.RUN:
                onContextRunClick(info.id);
                return true;
        }

        return (super.onOptionsItemSelected(item));
    }

    private final static int REQUEST_EDIT = 1001;


    private void onContextEditClick(long id)
    {
        Intent editIntent = new Intent(this, Wizzard_New1_General.class);
        JobInfo o = (JobInfo) _listAllJobs.getAdapter().getItem((int)id);
        final String setting = JobHandler.getSettings(JobHandler.getInstance().getByName(o.Name));
        editIntent.putExtra(Wizzard_New1_General.SETTINGS, setting);
        startActivityForResult(editIntent, REQUEST_EDIT);
    }


    private void onContextDeleteClick(long id)
    {
        JobInfo o = (JobInfo) _listAllJobs.getAdapter().getItem((int)id);
        JobHandler.getInstance().removeJobByName(o.Name);
        loadData();
    }


    private void onContextRunClick(long id)
    {
        JobInfo o = (JobInfo) _listAllJobs.getAdapter().getItem((int) id);
        JobHandler.getInstance().Do(this, o.Name);
    }


    void onButtonAllStartClick()
    {
        JobHandler.getInstance().resetStatusWhenFinished();
        JobHandler.getInstance().Do(this, false);
    }
}

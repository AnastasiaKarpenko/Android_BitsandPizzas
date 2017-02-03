package ws.tilda.anastasia.bitsandpizzas;

import android.app.Activity;
import android.app.Fragment;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.content.Intent;
import android.content.res.Configuration;
import android.os.Bundle;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.ShareActionProvider;

public class MainActivity extends Activity {

    private ShareActionProvider shareActionProvider;
    private String[] titles;
    private ListView drawerList;
    private DrawerLayout drawerLayout;
    private ActionBarDrawerToggle drawerToggle;
    private int currentPosition = 0;

    private class DrawerItemClickListener implements ListView.OnItemClickListener {

        @Override
        public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
            //Code to run when an item in the navigation drawer gets clicked
            selectItem(position);
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        titles = getResources().getStringArray(R.array.titles);
        drawerList = (ListView) findViewById(R.id.drawer);
        drawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout);

        //To initialize (fill in) the  ListView
        drawerList.setAdapter(new ArrayAdapter<String>(this,
                android.R.layout.simple_list_item_1, titles));
        drawerList.setOnItemClickListener(new DrawerItemClickListener());

        //Save instance of previous activity
        if (savedInstanceState != null) {
            currentPosition = savedInstanceState.getInt("position");
            setActionBarTitle(currentPosition);
        } else {
            selectItem(0); //top fragment is default
        }

        //Creation of ActionBarDrawerToggle
        drawerToggle = new ActionBarDrawerToggle(this, drawerLayout,
                R.string.open_drawer, R.string.close_drawer) {

            public void onDrawerClosed(View view) {
                super.onDrawerClosed(view);
                invalidateOptionsMenu();
            }

            public void onDrawerOpened(View drawerView) {
                super.onDrawerOpened(drawerView);
                invalidateOptionsMenu();
            }
        };

        drawerLayout.addDrawerListener(drawerToggle);

        //To enable the Up Button
        getActionBar().setDisplayHomeAsUpEnabled(true);
        getActionBar().setHomeButtonEnabled(true);

        //If back stack changes
        getFragmentManager().addOnBackStackChangedListener(
                new FragmentManager.OnBackStackChangedListener() {
                    @Override
                    public void onBackStackChanged() {
                        FragmentManager fragMan = getFragmentManager();
                        Fragment fragment = fragMan.findFragmentByTag("visible_fragment");
                        if (fragment instanceof TopFragment) {
                            currentPosition = 0;
                        } else if (fragment instanceof PizzaFragment) {
                            currentPosition = 1;
                        } else if (fragment instanceof PastaFragment) {
                            currentPosition = 2;
                        } else if (fragment instanceof StoresFragment) {
                            currentPosition = 3;
                        }

                        setActionBarTitle(currentPosition); //type right text on action bar
                        drawerList.setItemChecked(currentPosition, true); //check the choice in drawer
                    }
                }
        );
    }

    //This method works when the user selects the variant from the drawer list
    private void selectItem(int position) {
        currentPosition = position;
        Fragment fragment;

        switch(position) {
            case 1:
                fragment = new PizzaFragment();
                break;
            case 2:
                fragment = new PastaFragment();
                break;
            case 3:
                fragment = new StoresFragment();
                break;
            default:
                fragment = new TopFragment();
                break;
        }

        FragmentTransaction ft = getFragmentManager().beginTransaction();
        ft.replace(R.id.content_frame, fragment, "visible_fragment");
        ft.addToBackStack(null);
        ft.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_FADE);
        ft.commit();

        //Set the title of the action bar
        setActionBarTitle(position);

        //Closing drawer
        drawerLayout.closeDrawer(drawerList);
    }

    //If drawer is opened "sharing option" will not be visible
    @Override
    public boolean onPrepareOptionsMenu(Menu menu) {
        boolean drawerOpen = drawerLayout.isDrawerOpen(drawerList);
        menu.findItem(R.id.action_share).setVisible(!drawerOpen);

        return super.onPrepareOptionsMenu(menu);
    }

    //To sync the toggle to put it in the right position after creation of activity
    @Override
    protected void onPostCreate(Bundle savedInstanceState) {
        super.onPostCreate(savedInstanceState);
        drawerToggle.syncState();
    }

    //If configuration changed, toggle should know about it
    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
        drawerToggle.onConfigurationChanged(newConfig);
    }

    //Save the current position if the activity is killed
    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putInt("position", currentPosition);

    }


    public void setActionBarTitle(int position) {
        String title;

        if(position == 0) {
            title = getResources().getString(R.string.app_name);
        } else {
            title = titles[position];
        }
        getActionBar().setTitle(title);
    }

    //Put items from menu file to the action bar
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_main, menu);
        MenuItem menuItem = menu.findItem(R.id.action_share);
        shareActionProvider = (ShareActionProvider) menuItem.getActionProvider();
        setIntent("This is example text");
        return super.onCreateOptionsMenu(menu);
    }

    //For sharing function in the action bar
    private void setIntent(String text) {
        Intent intent = new Intent(Intent.ACTION_SEND);
        intent.setType("text/plain");
        intent.putExtra(Intent.EXTRA_TEXT, text);
        shareActionProvider.setShareIntent(intent);
    }

    //This method is called when the user clicks on the element in action bar
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        //If the user clicks on the drawerToggle
        if(drawerToggle.onOptionsItemSelected(item)) {
            return true;
        }

        //If the user clicks on the other options on Action bar
        switch(item.getItemId()) {
            case R.id.action_create_order:
                Intent intent = new Intent(this, OrderActivity.class);
                startActivity(intent);
                return true;
            case R.id.action_settings:

                return true;

            default:
                return super.onOptionsItemSelected(item);
        }
    }
}

package UI

import com.giladdev.wowchat.adaptors.SectionPageAdapter
import android.content.Intent
import android.graphics.Color
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.giladdev.wowchat.R
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import com.google.firebase.auth.FirebaseAuth
import kotlinx.android.synthetic.main.activity_dash_board.*

class DashBoardActivity : AppCompatActivity() {

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        super.onCreateOptionsMenu(menu)
        menuInflater.inflate(R.menu.main_menu, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        super.onOptionsItemSelected(item)

        if (item != null){
            if (item.itemId == R.id.logoutMenuID){
                // log the user out
                FirebaseAuth.getInstance().signOut()
                startActivity(Intent(this, MainActivity::class.java))
                finish()
            }
            if (item.itemId == R.id.settingMenuID){
                // take user to settings activity
                startActivity(Intent(this, SettingsActivity::class.java))
            }
        }


        return true
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_dash_board)

        lateinit var  sectionAdapter : SectionPageAdapter

        supportActionBar?.run{
            this.title = "Dashboard"
        }

        sectionAdapter = SectionPageAdapter(supportFragmentManager)

        dashBoardViewPager.adapter = sectionAdapter

        TabParent.setupWithViewPager(dashBoardViewPager)

        TabParent.setTabTextColors(Color.WHITE,Color.GREEN)


        var data = intent.extras

        data?.let{
            var username = it.get("name")

            Toast.makeText(this, username.toString(), Toast.LENGTH_LONG)
                    .show()
        }
        }
    }


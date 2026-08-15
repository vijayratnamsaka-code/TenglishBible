package com.tenglishbible.tenglishbible

import android.content.Intent
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.compose.BackHandler
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalClipboardManager
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalUriHandler
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import kotlinx.coroutines.delay
import org.json.JSONArray
import java.io.BufferedReader
import java.io.InputStreamReader

data class BibleVerse(
    val bookNumber: Int,
    val chapterNumber: Int,
    val verseNumber: Int,
    val text: String
)

data class BookInfo(
    val teluguName: String,
    val tenglishName: String
)

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val allVerses = loadBibleDataFromAssets()

        setContent {
            var isDarkMode by remember { mutableStateOf(false) }

            MaterialTheme(
                colorScheme = if (isDarkMode) darkColorScheme() else lightColorScheme()
            ) {
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    BibleApp(allVerses, isDarkMode) { isDarkMode = it }
                }
            }
        }
    }

    private fun loadBibleDataFromAssets(): List<BibleVerse> {
        val versesList = mutableListOf<BibleVerse>()
        try {
            val inputStream = assets.open("tenglish_bible.json")
            val reader = BufferedReader(InputStreamReader(inputStream))
            val jsonString = reader.readText()
            reader.close()

            val jsonArray = JSONArray(jsonString)
            for (i in 0 until jsonArray.length()) {
                val obj = jsonArray.getJSONObject(i)
                if (obj.has("book_number") && obj.has("chapter_number") && obj.has("text")) {
                    versesList.add(
                        BibleVerse(
                            bookNumber = obj.getInt("book_number"),
                            chapterNumber = obj.getInt("chapter_number"),
                            verseNumber = i + 1,
                            text = obj.getString("text")
                        )
                    )
                }
            }
        } catch (e: Exception) { e.printStackTrace() }
        return versesList
    }
}

val bookNamesMap = mapOf(
    1 to BookInfo("ఆదికాండము", "Adikaandamu"), 2 to BookInfo("నిర్గమకాండము", "Nirgamakaandamu"), 3 to BookInfo("లేవీయకాండము", "Leveyakaandamu"),
    4 to BookInfo("సంఖ్యాకాండము", "Sankhyakaandamu"), 5 to BookInfo("ద్వితీయోపదేశకాండము", "Dviteeyopadesha"), 6 to BookInfo("యెహోషువ", "Yehoshuva"),
    7 to BookInfo("న్యాయాధిపతులు", "Nyayadhipathulu"), 8 to BookInfo("రూతు", "Ruthu"), 9 to BookInfo("1 సమూయేలు", "1 Samuyelu"),
    10 to BookInfo("2 సమూయేలు", "2 Samuyelu"), 11 to BookInfo("1 రాజులు", "1 Rajulu"), 12 to BookInfo("2 రాజులు", "2 Rajulu"),
    13 to BookInfo("1 దినవృత్తాంతములు", "1 Dinavruthanthamulu"), 14 to BookInfo("2 దినవృత్తాంతములు", "2 Dinavruthanthamulu"), 15 to BookInfo("ఎజ్రా", "Ezra"),
    16 to BookInfo("నెహెమ్యా", "Nehemiya"), 17 to BookInfo("ఎస్తేరు", "Esteru"), 18 to BookInfo("యోబు", "Yobu"), 19 to BookInfo("కీర్తనలు", "Keerthanalu"),
    20 to BookInfo("సామెతలు", "Samethalu"), 21 to BookInfo("ప్రసంగి", "Prasangi"), 22 to BookInfo("పరమగీతము", "Parama Geethamu"),
    23 to BookInfo("యెషయా", "Yeshaya"), 24 to BookInfo("యిర్మియా", "Yirmiya"), 25 to BookInfo("విలాపవాక్యములు", "Vilapavaakyamulu"),
    26 to BookInfo("యెహెజ్కేలు", "Yehezkielu"), 27 to BookInfo("దానియేలు", "Daniyelu"), 28 to BookInfo("హోషేయ", "Hosheya"), 29 to BookInfo("యోవేలు", "Yovelu"),
    30 to BookInfo("ఆమోసు", "Amosu"), 31 to BookInfo("ఓబద్యా", "Obadya"), 32 to BookInfo("యోనా", "Yona"), 33 to BookInfo("మీకా", "Meeka"),
    34 to BookInfo("నాహూము", "Nahumu"), 35 to BookInfo("హబక్కూకు", "Habakkuku"), 36 to BookInfo("జెఫన్యా", "Zephanya"), 37 to BookInfo("హగ్గయి", "Haggayi"),
    38 to BookInfo("జెకర్యా", "Zekarya"), 39 to BookInfo("మలాకీ", "Malaki"), 40 to BookInfo("మత్తయి", "Mathayi"), 41 to BookInfo("మార్కు", "Marku"),
    42 to BookInfo("లూకా", "Luka"), 43 to BookInfo("యోహాను", "Yohanu"), 44 to BookInfo("అపొస్తలుల కార్యములు", "Acts"), 45 to BookInfo("రోమీయులకు", "Romiyulaku"),
    46 to BookInfo("1 కొరింథీయులకు", "1 Korinthiyulaku"), 47 to BookInfo("2 కొరింథీయులకు", "2 Korinthiyulaku"), 48 to BookInfo("గలతీయులకు", "Galathiyulaku"),
    49 to BookInfo("ఎఫెసీయులకు", "Ephesiyulaku"), 50 to BookInfo("ఫిలిప్పీయులకు", "Philippiyulaku"), 51 to BookInfo("కొలొస్సయులకు", "Kolossayulaku"),
    52 to BookInfo("1 థెస్సలొనీకయులకు", "1 Thessaloniyulaku"), 53 to BookInfo("2 థెస్సలొనీకయులకు", "2 Thessaloniyulaku"), 54 to BookInfo("1 తిమోతికి", "1 Thimothi"),
    55 to BookInfo("2 తిమోతికి", "2 Thimothi"), 56 to BookInfo("తీతుకు", "Theethu"), 57 to BookInfo("ఫిలేమోనుకు", "Philemonu"), 58 to BookInfo("హెబ్రీయులకు", "Hebreyulaku"),
    59 to BookInfo("యాకోబు", "Yaakobu"), 60 to BookInfo("1 పేతురు", "1 Pethuru"), 61 to BookInfo("2 పేతురు", "2 Pethuru"), 62 to BookInfo("1 యోహాను", "1 Yohanu"),
    63 to BookInfo("2 యోహాను", "2 Yohanu"), 64 to BookInfo("3 యోహాను", "3 Yohanu"), 65 to BookInfo("యూదా", "Yuda"), 66 to BookInfo("ప్రకటన గ్రంథము", "Prakatana")
)

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun BibleApp(allVerses: List<BibleVerse>, isDarkMode: Boolean, onThemeChange: (Boolean) -> Unit) {
    val context = LocalContext.current
    val clipboardManager = LocalClipboardManager.current
    val uriHandler = LocalUriHandler.current // Links open cheyadaniki idi vaadatham

    var currentScreen by remember { mutableStateOf("SPLASH") }
    var selectedBook by remember { mutableStateOf(1) }
    var selectedChapter by remember { mutableStateOf(1) }
    var targetVerseIndex by remember { mutableStateOf(0) }

    var isSearchActive by remember { mutableStateOf(false) }
    var searchQuery by remember { mutableStateOf("") }
    var showAboutUsDialog by remember { mutableStateOf(false) }

    var readingFontSize by remember { mutableStateOf(18.sp) }
    val highlightedVerses = remember { mutableStateMapOf<String, Color>() }
    var expandedVerseId by remember { mutableStateOf<String?>(null) }

    BackHandler(enabled = currentScreen != "SELECTION" || isSearchActive) {
        if (isSearchActive) {
            isSearchActive = false
            searchQuery = ""
        } else if (currentScreen == "VERSES") {
            currentScreen = "SELECTION"
        }
    }

    val selectedBookInfo = bookNamesMap[selectedBook] ?: BookInfo("Book $selectedBook", "")

    // --- CUSTOM ABOUT US DIALOG ---
    if (showAboutUsDialog) {
        Dialog(onDismissRequest = { showAboutUsDialog = false }) {
            Surface(
                shape = RoundedCornerShape(16.dp),
                color = Color.White,
                modifier = Modifier.fillMaxWidth()
            ) {
                // Ikkada verticalScroll add chesanu, button cut avvakunda
                Column(
                    modifier = Modifier
                        .padding(16.dp)
                        .verticalScroll(rememberScrollState()),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Spacer(modifier = Modifier.width(48.dp))
                        Text("About Tenglish Bible", fontWeight = FontWeight.Bold, fontSize = 20.sp, color = Color(0xFF001524))
                        IconButton(onClick = { showAboutUsDialog = false }) {
                            Icon(Icons.Filled.Close, contentDescription = "Close", tint = Color.Black)
                        }
                    }

                    HorizontalDivider(color = Color.LightGray.copy(alpha = 0.5f), modifier = Modifier.padding(bottom = 16.dp))

                    Image(
                        painter = painterResource(id = R.drawable.graceway_logo),
                        contentDescription = "Gracewaytruths Logo",
                        modifier = Modifier.size(120.dp).clip(CircleShape)
                    )

                    Spacer(modifier = Modifier.height(12.dp))

                    Text(
                        text = "Gracewaytruths is a Christian ministry dedicated to making God's Word accessible to everyone.\nThe Tenglish Bible was created to help people who cannot easily read Telugu but wish to understand the Holy Bible.",
                        textAlign = TextAlign.Center,
                        fontSize = 13.sp,
                        color = Color.DarkGray,
                        lineHeight = 18.sp,
                        modifier = Modifier.padding(horizontal = 8.dp)
                    )

                    Spacer(modifier = Modifier.height(16.dp))
                    HorizontalDivider(color = Color(0xFFDAA520).copy(alpha = 0.5f))
                    Spacer(modifier = Modifier.height(12.dp))

                    Row(
                        modifier = Modifier.fillMaxWidth().padding(horizontal = 16.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Box(
                            modifier = Modifier.size(50.dp).background(Color(0xFFE3F2FD), CircleShape),
                            contentAlignment = Alignment.Center
                        ) {
                            Icon(Icons.Filled.Person, contentDescription = "Developer", tint = Color(0xFF0D47A1), modifier = Modifier.size(30.dp))
                        }
                        Spacer(modifier = Modifier.width(16.dp))
                        Column {
                            Text("Developer", fontWeight = FontWeight.Bold, color = Color(0xFF0D47A1), fontSize = 16.sp)
                            Text("Saka Vijaya Ratnam", fontWeight = FontWeight.Bold, fontSize = 15.sp, color = Color.Black)
                            Row(verticalAlignment = Alignment.CenterVertically, modifier = Modifier.padding(top = 2.dp)) {
                                Icon(Icons.Filled.Phone, contentDescription = "Phone", modifier = Modifier.size(14.dp), tint = Color(0xFF0D47A1))
                                Spacer(modifier = Modifier.width(4.dp))
                                Text("+91 9553004889", fontSize = 14.sp, color = Color(0xFF0D47A1), fontWeight = FontWeight.Bold)
                            }
                        }
                    }

                    Spacer(modifier = Modifier.height(12.dp))
                    HorizontalDivider(color = Color(0xFFDAA520).copy(alpha = 0.5f))
                    Spacer(modifier = Modifier.height(12.dp))

                    Text("Please Support Gracewaytruths", fontWeight = FontWeight.Bold, color = Color(0xFF0D47A1), fontSize = 15.sp)
                    Spacer(modifier = Modifier.height(12.dp))

                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceEvenly
                    ) {
                        // YouTube Link
                        Column(
                            modifier = Modifier
                                .clickable { uriHandler.openUri("https://youtube.com/@gracewaytruths?si=39bX-qrZ0l1mwCoo") }
                                .padding(8.dp),
                            horizontalAlignment = Alignment.CenterHorizontally
                        ) {
                            Icon(Icons.Filled.PlayCircleFilled, contentDescription = "YouTube", tint = Color(0xFFFF0000), modifier = Modifier.size(36.dp))
                            Text("YouTube", fontWeight = FontWeight.Bold, fontSize = 12.sp, color = Color.Black)
                            Text("@gracewaytruths", fontSize = 10.sp, color = Color.DarkGray)
                        }
                        // Instagram Link
                        Column(
                            modifier = Modifier
                                .clickable { uriHandler.openUri("https://www.instagram.com/gracewaytruths?igsh=MWt2aW95dDhsbWc4Mw==") }
                                .padding(8.dp),
                            horizontalAlignment = Alignment.CenterHorizontally
                        ) {
                            Icon(Icons.Filled.CameraAlt, contentDescription = "Instagram", tint = Color(0xFFE1306C), modifier = Modifier.size(36.dp))
                            Text("Instagram", fontWeight = FontWeight.Bold, fontSize = 12.sp, color = Color.Black)
                            Text("@gracewaytruths", fontSize = 10.sp, color = Color.DarkGray)
                        }
                        // WhatsApp Link
                        Column(
                            modifier = Modifier
                                .clickable { uriHandler.openUri("https://wa.me/919553004889") }
                                .padding(8.dp),
                            horizontalAlignment = Alignment.CenterHorizontally
                        ) {
                            Icon(Icons.Filled.Call, contentDescription = "WhatsApp", tint = Color(0xFF25D366), modifier = Modifier.size(36.dp))
                            Text("WhatsApp", fontWeight = FontWeight.Bold, fontSize = 12.sp, color = Color.Black)
                            Text("+91 9553004889", fontSize = 10.sp, color = Color.DarkGray)
                        }
                    }

                    Spacer(modifier = Modifier.height(16.dp))

                    Box(
                        modifier = Modifier.fillMaxWidth().background(Color(0xFFF0F4FF), RoundedCornerShape(12.dp)).padding(12.dp)
                    ) {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Box(
                                modifier = Modifier.size(45.dp).background(Color(0xFF90CAF9), CircleShape),
                                contentAlignment = Alignment.Center
                            ) {
                                Icon(Icons.Filled.MenuBook, contentDescription = "Book", tint = Color(0xFF0D47A1))
                            }
                            Spacer(modifier = Modifier.width(12.dp))

                            Column(modifier = Modifier.weight(1f)) {
                                Text("Your support, prayers and encouragement help this ministry reach more people with the Word of God.", fontSize = 11.sp, color = Color.Black, lineHeight = 15.sp)
                                Text("❤ God Bless You! ❤", fontSize = 12.sp, fontWeight = FontWeight.Bold, color = Color(0xFF0D47A1), modifier = Modifier.padding(top = 4.dp))
                            }

                            Spacer(modifier = Modifier.width(8.dp))
                            Box(
                                modifier = Modifier.border(1.5.dp, Color(0xFF0D47A1), RoundedCornerShape(8.dp)).background(Color.White, RoundedCornerShape(8.dp)).padding(horizontal = 10.dp, vertical = 6.dp),
                                contentAlignment = Alignment.Center
                            ) {
                                Column(horizontalAlignment = Alignment.CenterHorizontally) {
                                    Text("Version", fontSize = 10.sp, color = Color(0xFF0D47A1), fontWeight = FontWeight.Bold)
                                    Text("1.0", fontSize = 18.sp, fontWeight = FontWeight.Bold, color = Color(0xFF0D47A1))
                                }
                            }
                        }
                    }

                    Spacer(modifier = Modifier.height(16.dp))

                    Button(
                        onClick = { showAboutUsDialog = false },
                        modifier = Modifier.fillMaxWidth().height(48.dp),
                        colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF003366)),
                        shape = RoundedCornerShape(8.dp)
                    ) {
                        Text("Close", color = Color.White, fontSize = 16.sp, fontWeight = FontWeight.Bold)
                    }
                }
            }
        }
    }
    // --- END ABOUT US DIALOG ---

    if (currentScreen == "SPLASH") {
        LaunchedEffect(Unit) {
            delay(2000)
            currentScreen = "SELECTION"
        }

        Box(
            modifier = Modifier.fillMaxSize(),
            contentAlignment = Alignment.Center
        ) {
            Image(
                painter = painterResource(id = R.drawable.splash_image),
                contentDescription = "App Logo",
                modifier = Modifier.fillMaxSize(),
                contentScale = ContentScale.Crop
            )
        }
    } else {
        Scaffold(
            topBar = {
                if (currentScreen == "VERSES") {
                    TopAppBar(
                        title = {
                            Text("${selectedBookInfo.tenglishName} $selectedChapter", fontWeight = FontWeight.Bold, fontSize = 18.sp)
                        },
                        actions = {
                            TextButton(onClick = { if(readingFontSize.value > 12f) readingFontSize = (readingFontSize.value - 2).sp }) {
                                Text("A-", color = MaterialTheme.colorScheme.onBackground, fontWeight = FontWeight.Bold)
                            }
                            TextButton(onClick = { if(readingFontSize.value < 36f) readingFontSize = (readingFontSize.value + 2).sp }) {
                                Text("A+", color = MaterialTheme.colorScheme.onBackground, fontWeight = FontWeight.Bold)
                            }
                            IconButton(onClick = { currentScreen = "SELECTION" }) {
                                Icon(Icons.Filled.Home, contentDescription = "Home")
                            }
                        },
                        colors = TopAppBarDefaults.topAppBarColors(containerColor = Color.Transparent)
                    )
                } else {
                    if (isSearchActive) {
                        TopAppBar(
                            title = {
                                TextField(
                                    value = searchQuery,
                                    onValueChange = { searchQuery = it },
                                    placeholder = { Text("Search any word...") },
                                    singleLine = true,
                                    colors = TextFieldDefaults.colors(
                                        focusedContainerColor = Color.Transparent,
                                        unfocusedContainerColor = Color.Transparent,
                                        focusedIndicatorColor = Color.Transparent,
                                        unfocusedIndicatorColor = Color.Transparent
                                    )
                                )
                            },
                            navigationIcon = {
                                IconButton(onClick = { isSearchActive = false; searchQuery = "" }) {
                                    Icon(Icons.Filled.ArrowBack, contentDescription = "Back")
                                }
                            },
                            actions = {
                                if (searchQuery.isNotEmpty()) {
                                    IconButton(onClick = { searchQuery = "" }) {
                                        Icon(Icons.Filled.Clear, contentDescription = "Clear")
                                    }
                                }
                            },
                            colors = TopAppBarDefaults.topAppBarColors(containerColor = MaterialTheme.colorScheme.surfaceVariant)
                        )
                    } else {
                        TopAppBar(
                            title = { Text("Tenglish Bible", fontWeight = FontWeight.Bold) },
                            actions = {
                                IconButton(onClick = { isSearchActive = true }) {
                                    Icon(Icons.Filled.Search, contentDescription = "Search")
                                }
                                IconButton(onClick = { onThemeChange(!isDarkMode) }) {
                                    Icon(
                                        imageVector = if (isDarkMode) Icons.Filled.LightMode else Icons.Filled.DarkMode,
                                        contentDescription = "Theme Toggle"
                                    )
                                }
                                IconButton(onClick = { showAboutUsDialog = true }) {
                                    Icon(Icons.Filled.Info, contentDescription = "About Us")
                                }
                            },
                            colors = TopAppBarDefaults.topAppBarColors(containerColor = MaterialTheme.colorScheme.surfaceVariant)
                        )
                    }
                }
            }
        ) { paddingValues ->
            Box(modifier = Modifier.padding(paddingValues).fillMaxSize()) {

                when (currentScreen) {

                    "SELECTION" -> {
                        Column(modifier = Modifier.fillMaxSize()) {
                            if (isSearchActive && searchQuery.isNotEmpty()) {
                                val searchResults = allVerses.filter { it.text.contains(searchQuery, ignoreCase = true) }
                                LazyColumn(modifier = Modifier.fillMaxSize().padding(16.dp)) {
                                    item { Text("Found ${searchResults.size} verses", fontWeight = FontWeight.Bold, modifier = Modifier.padding(bottom = 8.dp)) }
                                    items(searchResults) { verse ->
                                        val bName = bookNamesMap[verse.bookNumber]?.tenglishName ?: ""
                                        Card(
                                            modifier = Modifier
                                                .fillMaxWidth()
                                                .padding(bottom = 8.dp)
                                                .clickable {
                                                    selectedBook = verse.bookNumber
                                                    selectedChapter = verse.chapterNumber
                                                    targetVerseIndex = verse.verseNumber - 1
                                                    isSearchActive = false
                                                    searchQuery = ""
                                                    currentScreen = "VERSES"
                                                },
                                            colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surfaceVariant)
                                        ) {
                                            Column(modifier = Modifier.padding(12.dp)) {
                                                Text(verse.text, fontSize = 16.sp)
                                                Text("- $bName ${verse.chapterNumber}:${verse.verseNumber}", color = MaterialTheme.colorScheme.primary, fontSize = 14.sp, modifier = Modifier.align(Alignment.End))
                                            }
                                        }
                                    }
                                }
                            } else {
                                Row(
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .background(MaterialTheme.colorScheme.surfaceVariant)
                                        .padding(vertical = 8.dp, horizontal = 4.dp)
                                ) {
                                    Text("Book", modifier = Modifier.weight(0.6f), fontSize = 14.sp, fontWeight = FontWeight.Bold, textAlign = TextAlign.Center)
                                    Text("Ch.", modifier = Modifier.weight(0.2f), fontSize = 14.sp, fontWeight = FontWeight.Bold, textAlign = TextAlign.Center)
                                    Text("Ver.", modifier = Modifier.weight(0.2f), fontSize = 14.sp, fontWeight = FontWeight.Bold, textAlign = TextAlign.Center)
                                }
                                HorizontalDivider()

                                Row(modifier = Modifier.fillMaxSize()) {
                                    LazyColumn(modifier = Modifier.weight(0.6f).fillMaxHeight()) {
                                        items((1..66).toList()) { bookNum ->
                                            val isSelected = bookNum == selectedBook
                                            val info = bookNamesMap[bookNum] ?: BookInfo("Book $bookNum", "")
                                            Box(
                                                modifier = Modifier
                                                    .fillMaxWidth()
                                                    .background(if (isSelected) MaterialTheme.colorScheme.secondaryContainer else Color.Transparent)
                                                    .clickable { selectedBook = bookNum; selectedChapter = 1 }
                                                    .padding(vertical = 12.dp, horizontal = 12.dp)
                                            ) {
                                                Column {
                                                    Text(info.tenglishName, fontSize = 16.sp, fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Normal)
                                                    Text(info.teluguName, fontSize = 13.sp, color = MaterialTheme.colorScheme.onSurfaceVariant)
                                                }
                                            }
                                            HorizontalDivider(color = Color.LightGray.copy(alpha = 0.3f))
                                        }
                                    }
                                    VerticalDivider(thickness = 1.dp)

                                    val chaptersInBook = allVerses.filter { it.bookNumber == selectedBook }.map { it.chapterNumber }.distinct().sorted()
                                    LazyColumn(modifier = Modifier.weight(0.2f).fillMaxHeight()) {
                                        items(chaptersInBook.ifEmpty { (1..50).toList() }) { chNum ->
                                            val isSelected = chNum == selectedChapter
                                            Box(
                                                modifier = Modifier
                                                    .fillMaxWidth()
                                                    .background(if (isSelected) MaterialTheme.colorScheme.secondaryContainer else Color.Transparent)
                                                    .clickable { selectedChapter = chNum }
                                                    .padding(vertical = 12.dp),
                                                contentAlignment = Alignment.Center
                                            ) { Text("$chNum", fontSize = 16.sp, fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Normal) }
                                            HorizontalDivider(color = Color.LightGray.copy(alpha = 0.3f))
                                        }
                                    }
                                    VerticalDivider(thickness = 1.dp)

                                    val versesInChapter = allVerses.filter { it.bookNumber == selectedBook && it.chapterNumber == selectedChapter }
                                    LazyColumn(modifier = Modifier.weight(0.2f).fillMaxHeight()) {
                                        itemsIndexed(versesInChapter.ifEmpty { List(30) { null } }) { index, _ ->
                                            Box(
                                                modifier = Modifier
                                                    .fillMaxWidth()
                                                    .clickable {
                                                        targetVerseIndex = index
                                                        currentScreen = "VERSES"
                                                    }
                                                    .padding(vertical = 12.dp),
                                                contentAlignment = Alignment.Center
                                            ) { Text("${index + 1}", fontSize = 16.sp) }
                                            HorizontalDivider(color = Color.LightGray.copy(alpha = 0.3f))
                                        }
                                    }
                                }
                            }
                        }
                    }

                    "VERSES" -> {
                        val currentChapterVerses = allVerses.filter { it.bookNumber == selectedBook && it.chapterNumber == selectedChapter }
                        val maxCh = allVerses.filter { it.bookNumber == selectedBook }.maxOfOrNull { it.chapterNumber } ?: 1
                        val listState = rememberLazyListState()
                        val highlightColors = listOf(Color(0xFFFFF59D), Color(0xFFF48FB1), Color(0xFFA5D6A7), Color(0xFF90CAF9), Color.Transparent)

                        LaunchedEffect(targetVerseIndex) {
                            if (targetVerseIndex in currentChapterVerses.indices) {
                                listState.scrollToItem(targetVerseIndex)
                            }
                        }

                        Box(modifier = Modifier.fillMaxSize()) {

                            LazyColumn(state = listState, modifier = Modifier.fillMaxSize().padding(horizontal = 16.dp, vertical = 4.dp)) {
                                itemsIndexed(currentChapterVerses) { index, verse ->
                                    val displayVerseNum = index + 1
                                    val verseKey = "${verse.bookNumber}_${verse.chapterNumber}_${displayVerseNum}"
                                    val bgColor = highlightedVerses[verseKey] ?: Color.Transparent
                                    val isExpanded = expandedVerseId == verseKey

                                    Column(modifier = Modifier.padding(bottom = 8.dp)) {
                                        Box(
                                            modifier = Modifier
                                                .fillMaxWidth()
                                                .background(bgColor, RoundedCornerShape(4.dp))
                                                .clickable { expandedVerseId = if (isExpanded) null else verseKey }
                                                .padding(4.dp)
                                        ) {
                                            Text(
                                                text = "$displayVerseNum. ${verse.text}",
                                                fontSize = readingFontSize,
                                                lineHeight = (readingFontSize.value + 10).sp
                                            )
                                        }

                                        if (isExpanded) {
                                            Row(
                                                modifier = Modifier
                                                    .fillMaxWidth()
                                                    .background(MaterialTheme.colorScheme.surfaceVariant, RoundedCornerShape(8.dp))
                                                    .padding(8.dp),
                                                horizontalArrangement = Arrangement.SpaceAround,
                                                verticalAlignment = Alignment.CenterVertically
                                            ) {
                                                Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                                                    highlightColors.forEach { color ->
                                                        Box(
                                                            modifier = Modifier
                                                                .size(24.dp)
                                                                .clip(CircleShape)
                                                                .background(if (color == Color.Transparent) Color.Gray.copy(alpha = 0.3f) else color)
                                                                .clickable {
                                                                    highlightedVerses[verseKey] = color
                                                                    expandedVerseId = null
                                                                }
                                                        )
                                                    }
                                                }

                                                IconButton(onClick = {
                                                    clipboardManager.setText(AnnotatedString("${bookNamesMap[verse.bookNumber]?.tenglishName} ${verse.chapterNumber}:$displayVerseNum\n${verse.text}"))
                                                    expandedVerseId = null
                                                }, modifier = Modifier.size(32.dp)) {
                                                    Icon(Icons.Filled.ContentCopy, contentDescription = "Copy")
                                                }

                                                IconButton(onClick = {
                                                    val sendIntent = Intent().apply {
                                                        action = Intent.ACTION_SEND
                                                        putExtra(Intent.EXTRA_TEXT, "📖 ${bookNamesMap[verse.bookNumber]?.tenglishName} ${verse.chapterNumber}:$displayVerseNum\n\n${verse.text}\n\n- Shared via Tenglish Bible App")
                                                        type = "text/plain"
                                                    }
                                                    context.startActivity(Intent.createChooser(sendIntent, "Share Verse"))
                                                    expandedVerseId = null
                                                }, modifier = Modifier.size(32.dp)) {
                                                    Icon(Icons.Filled.Share, contentDescription = "Share")
                                                }
                                            }
                                        }
                                        HorizontalDivider(modifier = Modifier.padding(top = 8.dp), color = Color.LightGray.copy(alpha = 0.3f))
                                    }
                                }
                            }

                            Row(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .align(Alignment.BottomCenter)
                                    .padding(bottom = 24.dp, start = 12.dp, end = 12.dp),
                                horizontalArrangement = Arrangement.SpaceBetween,
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                if (selectedChapter > 1) {
                                    IconButton(
                                        onClick = { selectedChapter--; targetVerseIndex = 0 },
                                        modifier = Modifier.size(56.dp).background(Color.Transparent)
                                    ) {
                                        Icon(
                                            Icons.Filled.KeyboardArrowLeft,
                                            contentDescription = "Previous",
                                            modifier = Modifier.size(40.dp),
                                            tint = MaterialTheme.colorScheme.primary.copy(alpha = 0.7f)
                                        )
                                    }
                                } else {
                                    Spacer(modifier = Modifier.size(56.dp))
                                }

                                if (selectedChapter < maxCh) {
                                    IconButton(
                                        onClick = { selectedChapter++; targetVerseIndex = 0 },
                                        modifier = Modifier.size(56.dp).background(Color.Transparent)
                                    ) {
                                        Icon(
                                            Icons.Filled.KeyboardArrowRight,
                                            contentDescription = "Next",
                                            modifier = Modifier.size(40.dp),
                                            tint = MaterialTheme.colorScheme.primary.copy(alpha = 0.7f)
                                        )
                                    }
                                } else {
                                    Spacer(modifier = Modifier.size(56.dp))
                                }
                            }
                        }
                    }
                }
            }
        }
    }
}
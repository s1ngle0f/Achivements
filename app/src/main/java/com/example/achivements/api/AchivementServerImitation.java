package com.example.achivements.api;

import static android.content.Context.MODE_PRIVATE;

import android.content.Context;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.example.achivements.MainActivity;
import com.example.achivements.models.Achivement;
import com.example.achivements.models.Comment;
import com.example.achivements.models.User;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Random;

public class AchivementServerImitation implements IAchivementServer{
    private SQLiteDatabase db;
    private User user;
    private HashMap<Integer, User> users = new HashMap<>();
    private HashMap<Integer, String> passwords = new HashMap<>();
    private HashMap<Integer, ArrayList<String>> accessCodes = new HashMap<>();

    public AchivementServerImitation(User user) {
        this.user = user;
        Gson gson = new Gson();
        HashMap<Integer, User> _users =
                gson.fromJson(MainActivity.ServerSharedPreferences.getString("users", ""),
                        new TypeToken<HashMap<Integer, User>>(){}.getType());

        HashMap<Integer, String> _passwords =
                gson.fromJson(MainActivity.ServerSharedPreferences.getString("passwords", ""),
                        new TypeToken<HashMap<Integer, String>>(){}.getType());

        HashMap<Integer, ArrayList<String>> _accessCodes =
                gson.fromJson(MainActivity.ServerSharedPreferences.getString("accessCodes", ""),
                        new TypeToken<HashMap<Integer, ArrayList<String>>>(){}.getType());
        if(_users != null) {
            users = _users;
            if(_passwords != null)
                passwords = _passwords;
            if(_accessCodes != null)
                accessCodes = _accessCodes;
            for (User _user : users.values()) {
                _user.ResetFriends(GetUsers(_user.getFriendIds()));
//                _user.ResetFriends();
            }
        }
        else{
            System.out.println("DATA BASED CREATED TRY!!!");
            passwords.clear();
            accessCodes.clear();
            for(int i = 10; i < 30; i++){
                User _user = new User("u" + i, null, "any_description" + i);
                _user.AddAchivement(new Achivement("TODO Something any man" + i, Achivement.Status.COMPLETED, _user));
                _user.AddAchivement(new Achivement("TODO Something any man" + i, Achivement.Status.ACTIVE, _user).AddComment(new Comment(_user.getId(), "Some comment")));
                users.put(_user.getId(), _user);
                passwords.put(_user.getId(), "123");
            }
        }
        System.out.println(users.size());
    }
    static boolean isFirstLaunch = true;
    public void initDB(Context context){
        db = context.openOrCreateDatabase("app.db", Context.MODE_PRIVATE, null);
        db.execSQL("CREATE TABLE IF NOT EXISTS tasks (name TEXT)");
        String[] activities = {
                "Познакомьтесь с новым человеком и узнайте о его увлечениях.",
                "Устройте мероприятие с друзьями и позовите их всех.",
                "Примите участие в благотворительной акции.",
                "Проведите вечер с друзьями без использования смартфонов.",
                "Попробуйте себя в роли волонтера.",
                "Участвуйте в местном клубе или организации.",
                "Попробуйте заняться социальными танцами или групповыми уроками танцев.",
                "Организуйте пикник в парке и позовите знакомых.",
                "Проведите день, устраивая случайные добрые дела для незнакомых людей.",
                "Запишитесь на курсы общественных выступлений.",
                "Посетите местное кафе и попробуйте завести разговор с незнакомым человеком.",
                "Участвуйте в обсуждении книги или фильма в местной библиотеке.",
                "Пойдите на вечеринку с тематическим костюмом.",
                "Проведите вечер с друзьями, играя в настольные игры.",
                "Попробуйте заняться искусством: рисованием, скульптурой, или ремесленным творчеством.",
                "Проведите интервью с близкими друзьями, чтобы узнать о их жизни и интересах.",
                "Участвуйте во встрече бывших выпускников школы или университета.",
                "Пойдите на концерт или выставку и попробуйте обсудить произведения искусства с другими посетителями.",
                "Создайте собственный блог или влог и начните общение с подписчиками.",
                "Проведите день без использования социальных сетей.",
                "Посетите местный спортивный клуб и познакомьтесь с единомышленниками.",
                "Устройте домашний киносеанс и пригласите друзей на просмотр фильма.",
                "Пойдите на лекцию или семинар по интересующей вас теме.",
                "Запишитесь в команду по какому-либо виду спорта или фитнеса.",
                "Пойдите на мастер-класс по чему-либо, что вас интересует.",
                "Организуйте встречу со старыми друзьями из детства.",
                "Познакомьтесь с новыми соседями.",
                "Помогите соседям с какой-либо задачей или проблемой.",
                "Примите участие в кулинарном курсе или кулинарном клубе.",
                "Попробуйте себя в караоке и пригласите друзей.",
                "Организуйте игровой вечер с друзьями.",
                "Примите участие в местном театральном спектакле.",
                "Пойдите на местный ярмарку или фестиваль.",
                "Участвуйте в занятиях йогой или медитацией в группе.",
                "Запишитесь на курсы обучения иностранным языкам.",
                "Пойдите на волонтерские мероприятия в приютах для животных.",
                "Проведите день в парке, играя в фрисби или бадминтон с незнакомыми людьми.",
                "Позовите друзей на вечеринку \"Караоке с песнями из детства\".",
                "Участвуйте в местных спортивных мероприятиях или соревнованиях.",
                "Пойдите на летний фестиваль музыки и искусства.",
                "Пойдите на местный рынок и познакомьтесь с продавцами.",
                "Попробуйте себя в групповом походе на природу.",
                "Организуйте игру в мафию с друзьями.",
                "Познакомьтесь с коллегами по работе вне офиса.",
                "Устройте домашний концерт и пригласите друзей, играющих на музыкальных инструментах.",
                "Примите участие в благотворительном забеге или велопробеге.",
                "Пойдите на мастер-класс по готовке блюд другой кухни.",
                "Проведите вечеринку \"Фильмы из детства\" и обсудите их с друзьями.",
                "Пойдите на лекцию или семинар по предпринимательству.",
                "Устройте вечеринку с маскарадом.",
                "Проведите день, делая комплименты незнакомым людям.",
                "Пойдите на выставку искусства местного художника и поговорите с ним.",
                "Позовите друзей на ужин и приготовьте блюда из разных культур.",
                "Организуйте мастер-класс по созданию чего-то интересного (например, украшений или керамики).",
                "Пойдите на мероприятие по интересующей вас теме в местной библиотеке.",
                "Проведите день, делая добрые дела для своих друзей.",
                "Познакомьтесь с соседями в спортивном зале или бассейне.",
                "Пойдите на лекцию или дискуссию о социальных вопросах.",
                "Устройте день игр на свежем воздухе с семьей и друзьями.",
                "Пойдите на выставку ретроавтомобилей и познакомьтесь с владельцами.",
                "Участвуйте в местном хоре или музыкальной группе.",
                "Проведите день, проводя интервью с пожилыми людьми и записывая их истории.",
                "Пойдите на мероприятие для знакомства с местными предпринимателями.",
                "Примите участие в местном марафоне или забеге.",
                "Устройте вечеринку \"Путешествие в прошлое\" и попробуйте рассказать другим о своем прошлом.",
                "Пойдите на местный фермерский рынок и познакомьтесь с продавцами органических продуктов.",
                "Познакомьтесь с соседями на детской площадке.",
                "Пойдите на лекцию о том, как развивать лидерские качества.",
                "Устройте вечеринку \"Времена года\", на которой каждый гость представляет одну из четырех пор времени года.",
                "Проведите день, участвуя в мероприятиях по защите окружающей среды.",
                "Пойдите на мастер-класс по фотографии и делитесь своими работами с другими.",
                "Позовите друзей на игру в настольный теннис или пинг-понг.",
                "Устройте день игр в парке или на пляже с незнакомыми людьми.",
                "Пойдите на местный культурный фестиваль.",
                "Организуйте вечеринку \"Страны мира\" и приготовьте блюда из разных кухонь.",
                "Примите участие в местной группе по изучению истории или культуры.",
                "Пойдите на лекцию или семинар о саморазвитии.",
                "Устройте вечеринку \"Звезды и знаменитости\" и пригласите друзей в ролях знаменитостей.",
                "Проведите день, помогая в местном приюте для бездомных.",
                "Познакомьтесь с участниками местного молодежного клуба.",
                "Пойдите на мастер-класс по танцам.",
                "Пойдите на лекцию или семинар по психологии.",
                "Устройте вечеринку \"Детские игры\" и играйте в игры из детства.",
                "Позовите друзей на мастер-класс по приготовлению десертов.",
                "Проведите день, помогая старшим людям в местном центре пожилых.",
                "Пойдите на мероприятие по изучению местной истории и культуры.",
                "Устройте вечеринку \"Путешествие в будущее\" и обсудите свои мечты и цели.",
                "Пойдите на лекцию или семинар по искусству коммуникации.",
                "Познакомьтесь с участниками местного театрального кружка.",
                "Пойдите на мастер-класс по изготовлению поделок.",
                "Проведите день, помогая детям в местном детском доме или школе.",
                "Позовите друзей на мастер-класс по созданию музыки или песен.",
                "Устройте вечеринку \"Загадки и головоломки\" и играйте в головоломки.",
                "Пойдите на мероприятие по изучению культурных различий.",
                "Пойдите на лекцию или семинар по современным тенденциям в искусстве и дизайне.",
                "Познакомьтесь с участниками местной группы по спортивным играм.",
                "Устройте вечеринку \"Ретро\" и обсудите старые фотографии и воспоминания.",
                "Пойдите на мастер-класс по кино или видеомонтажу.",
                "Проведите день, помогая организации, занимающейся защитой прав животных.",
                "Пойдите на местный фестиваль"
        };
        if(isFirstLaunch) {
            for (String task : activities) {
                String insertQuery = "INSERT INTO tasks (name) VALUES ('" + task + "');";
                db.execSQL(insertQuery);
            }
            isFirstLaunch = false;
        }
    }

    public void Destructor(){
        Gson gson = new Gson();
        SharedPreferences sharedPreferences = MainActivity.mainActivity.getSharedPreferences("serverSettings", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString("users", gson.toJson(users));
        editor.putString("passwords", gson.toJson(passwords));
        editor.putString("accessCodes", gson.toJson(accessCodes));
        editor.putString("test", "debil");
        editor.apply();
    }
    public HashMap<Integer, User> getUsers() {
        return users;
    }

    public void setUsers(HashMap<Integer, User> users) {
        this.users = users;
    }

    public HashMap<Integer, String> getPasswords() {
        return passwords;
    }

    public void setPasswords(HashMap<Integer, String> passwords) {
        this.passwords = passwords;
    }

    public HashMap<Integer, ArrayList<String>> getAccessCodes() {
        return accessCodes;
    }

    public void setAccessCodes(HashMap<Integer, ArrayList<String>> accessCodes) {
        this.accessCodes = accessCodes;
    }

    @Override
    public User GetUserInfo(String login, String accessToken) {
        for (User _user : users.values()) {
            if(accessCodes.get(_user.getId()) != null){
                if (_user.getLogin().equals(login) && accessCodes.get(_user.getId()).contains(accessToken)) {
                    return _user;
                }
            }
        }
        return null;
    }

    @Override
    public boolean IsExistUser() {
        return false;
    }

    @Override
    public boolean IsExistUser(String login, String password) {
        return false;
    }

    @Override
    public String CreateUser(String login, String password) {
        User user = new User(login);
        if(findUser(user.getLogin()) == null) {
            users.put(user.getId(), user);
            passwords.put(user.getId(), password);
            String accessToken = GetAccessCode(login, password);
            return accessToken;
        }
        return null;
    }

    private User findUser(String login){
        for (User user : users.values()) {
            if(user.getLogin().equals(login))
                return user;
        }
        return null;
    }

    @Override
    public Achivement GetNewAchivement() {
        String selectQuery = "SELECT * FROM tasks;";
        Cursor cursor = db.rawQuery(selectQuery, null);
        ArrayList<String> tasks = new ArrayList<>();

        if (cursor.moveToFirst()) {
            do {
                tasks.add(cursor.getString(0));
            } while (cursor.moveToNext());
        }

        cursor.close();
        return new Achivement(tasks.get((int)(Math.random()*tasks.size())), Achivement.Status.ACTIVE);
    }

    @Override
    public ArrayList<Achivement> GetAchivements(int count) {
        return null;
    }

    @Override
    public String GetAccessCode(String login, String password) {
        for (User _user : users.values()) {
            if(_user.getLogin().equals(login) && passwords.get(_user.getId()).equals(password)) {
                String newToken = createToken(16);
//                _user.setAccessToken(newToken);
                if(accessCodes.get(_user.getId()) == null)
                    accessCodes.put(_user.getId(), new ArrayList<String>());
                accessCodes.get(_user.getId()).add(newToken);
                return newToken;
            }
        }
        return null;
    }

    @Override
    public void EditAchivement(Achivement achivement) {

    }

    @Override
    public void EditUser(User user) {
        if(user != null){
            users.put(user.getId(), user);
        }
    }

    @Override
    public User AddFriend(String login, String accessToken, User friend) {
        for (User _user : users.values()) {
            if(accessCodes.get(_user.getId()) != null){
                if (_user.getLogin().equals(login) && accessCodes.get(_user.getId()).contains(accessToken)) {
                    _user.AddFriend(friend);
                    return _user;
                }
            }
        }
        return null;
    }

    @Override
    public ArrayList<User> GetUsers() {
        int count = 10;
        return GetUsers(count);
    }

    public ArrayList<User> GetUsers(ArrayList<Integer> ids) {
        ArrayList<User> _users = new ArrayList<>();
        for (int id : ids) {
            if(users.get(id) != null)
                _users.add(users.get(id));
        }
        return _users;
    }

    public User GetUser(int id) {
        return users.get(id);
    }

    @Override
    public ArrayList<User> GetUsers(int count) {
        ArrayList<User> _users = new ArrayList<>();
        int counter = 0;
        for (User _user : users.values()) {
            if(counter >= count)
                break;
            _users.add(_user);
            counter++;
        }
        return _users;
    }

    @Override
    public ArrayList<User> GetUsers(String name) {
        ArrayList<User> _users = new ArrayList<>();
        for (User _user : users.values()) {
            if(_user.getLogin().contains(name))
                _users.add(_user);
        }
        return _users;
    }

    private String createToken(int length){
        String chars = "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789_";
        String res = "";
        for(int i = 0; i < length; i++){
            res += chars.toCharArray()[(int)(Math.random()*chars.length())];
        }
        return res;
    }
}

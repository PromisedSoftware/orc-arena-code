package LanguageStrings;

/**
 * Class containing translations
 * and change language
 */
public class Language {

    /**
     * Every word that is in the main menu
     */
    public static class Menu{

        public static String newGame ="NEW GAME";
        public static String howToPlay ="HOW tO PLAY";
        public static String continueGame="CONTINUE";
        public static String cheats ="CHEATS";
        public static String settings="SETTINGS";
        public static String exit="EXIT";
        public static String language="Language";
        public static String resolution="Resolution";
        public static String actorQuality="Characters texture quality";
        public static String terrainQuality="Terrain texture quality";
        public static String[] textures={"Low","Medium","High","Ultra"};


        /**
         * Sets main menu language as English
         */
        public static void setEnglish(){
            newGame ="NEW GAME";
            continueGame="CONTINUE";
            howToPlay ="HOW TO PLAY";
            cheats ="CHEATS";
            exit="EXIT";
            settings="SETTINGS";

            language="Language";
            resolution="Resolution";
            actorQuality="Characters texture quality";
            terrainQuality="Terrain texture quality";
            textures[0]="Low";
            textures[1]="Medium";
            textures[2]="High";
            textures[3]="Ultra";
        }

        /**
         * Sets main menu language as Polish
         */
        public static void setPolish(){
            newGame ="NOWA GRA";
            howToPlay ="JAK GRAĆ";
            continueGame="KONTYNUUJ";
            cheats ="OSZUSTWA";
            exit="WYJDŹ";
            settings="USTAWIENIA";

            language="Język";
            resolution="Rozdzielczość";
            actorQuality="Jakość postaci";
            terrainQuality="Jakość terenu";
            textures[0]="Niska";
            textures[1]="Średnia";
            textures[2]="Wysoka";
            textures[3]="Ultra";
        }
    }

    /**
     * Every description when selecting player class
     */
    public static class PlayerPicker{

        public static String warrior=
                "<html>Warriors wearing rusty plate armors<br>" +
                "carrying heavy,unwieldy swords. Some<br>" +
                "might say that pigs will fly sooner than<br>" +
                "warrior will reach destination." +
                "but when He does, He becomes unstoppable" +
                "machine of death.<br>"+
                "Class in progress:No visual effects"+"</html>";

        public static String mage=
                "<html>Mages can't wield heavy weapons<br>" +
                "inexperienced are weak but those who<br>" +
                "survived become masters of life and death</html>";

        public static String agent =
                "<html>They are twisted, vile... fascinating<br> " +
                        "traits in those times. They make friends<br>" +
                        "unless have no gold</html>";

        public static String selectCharacter="SELECT YOUR CHARACTER";


        //- - - - - - - - -- -
        public static void setPolish(){
            warrior= "<html>Wojownicy w pordzewiałych blachach,<br>" +
                    "ciężkim, nieporęcznym mieczem. Niektórzy<br>" +
                    "powiedzą, że prędzej mucha w smole<br>" +
                    "przegoni zająca niż wojownik dotrze<br>" +
                    "do celu. Ale kiedy to zrobi staje<br>" +
                    "się niepowstrzymaną maszyną do zabijania</html>";

            mage=   "<html>Magowie nie potrafią utrzymać<br>" +
                    "miecza,a co dopiero wygrać bitę.<br>" +
                    "Ale ci potężni zamieniają pole bitwy<br>" +
                    "w pole śmierci</html>";

            agent =
                    "<html>Agenci są bezwzględni, podli... fascynujące<br> " +
                            "cechy w tych czasach. Potrafią się<br>" +
                            "zaprzyjaźnić, dopóki mają złoto.</html>";

            selectCharacter="WYBIERZ SWOJĄ POSTAĆ";
        }

        public static void setEnglish(){
            warrior= "<html>Warriors wear rusty plate armors<br>" +
                            "carrying heavy,unwieldy swords. Some<br>" +
                            "might say that pigs will fly sooner than<br>" +
                            "warrior will reach destination.<br>" +
                            "but when He does, He becomes unstoppable<br>" +
                            "machine of death.</html>";

             mage= "<html>Mages can't wield a sword. Can't<br>" +
                     "even wield a sword, let alone win<br>" +
                     "a battle. But those mighty ones turns<br>" +
                     "battle field into a field of death<br></html><br>";

            agent =
                    "<html>They are twisted, vile... fascinating<br> " +
                            "traits in those times. They make friends<br>" +
                            "unless have no money</html>";

            selectCharacter="SELECT YOUR CHARACTER";
        }
    }

    /**
     * Every word that is used to describe skills
     */
    public static class Skills{

        public static String[] Q_Warr={"Increases attack damage and heal the same amount while this skill is active by: "," damage"};
        public static String[] W_Warr={"Increases armor by: "," for : "};
        public static String E_Warr="Increases range of next basic attack and hits all enemies in attack range.";

        public static String[] Q_Mage={"Cast fireball that deals: ","damage to enemies"};
        public static String[] W_Mage={"Creates magic barrier for: "," that absorbs damage from enemies and electrocutes them for : "," damage"};
        public static String E_Mage="Heals up for: ";

        public static String Q_Agent ="Next attack deal additional damage: ";
        public static String W_Agent ="Make friends with surrounding foes. When foe kill someone You receive experience, click on friend to make him foe again";
        public static String E_Agent= "Teleport to any place on the map";

        /**
         * Set skills description to Polish language
         */
        private static void setPolish(){
            Q_Warr[0]="Kiedy ta umiejętność jest aktywna zwiększa obrażenia ataków i leczy się o: ";
            Q_Warr[1]=" obrażeń";
            W_Warr[0]="Zwiększa pancerz o: ";
            W_Warr[1]=" na czas : ";
            E_Warr="Następny atak podstawowy ma zwiększony zasięg i zadaje obrażenia wszystkim w zasięgu ataku.";

            Q_Mage[0]="Kula ognia która zadaje: ";
            Q_Mage[1]=" obrażeń wrogom";
            W_Mage[0]="Tworzy magiczną barierę na czas: ";
            W_Mage[1]=" która absorbuje obrażenia i poraża ich : ";
            W_Mage[2]=" obrażeń";
            E_Mage="Leczy za: ";

            Q_Agent ="Następny atak zadaje zwiększone obrażenia o: ";
            W_Agent= "Zaprzyjaźnia się z otaczającymi wrogami. kiedy zaprzyjaźniony zabije kogoś otrzymujesz za niego doświadczenie." +
                    "Kliknij na przyjaciela, aby uczynić go znowu wrogiem";

            E_Agent= "Teleport do dowolnego miejsca na mapie";
        }

        /**
         * Set skills description to English language
         */
        private static void setEnglish(){
            Q_Warr[0]="Increases attack damage and heal the same amount while this skill is active by:: ";
            Q_Warr[1]=" damage";
            W_Warr[0]="Increases armor by: ";
            W_Warr[1]=" for : ";
            E_Warr="Increases range of next basic attack and hits all enemies in attack range.";

            Q_Mage[0]="Cast fireball that deals: ";
            Q_Mage[1]="damage to enemies";
            W_Mage[0]="Creates magic barrier for: ";
            W_Mage[1]=" that absorbs damage from enemies and electrocutes them for: ";
            W_Mage[2]=" damage";
            E_Mage="Heals up for: ";

            Q_Agent ="Next attack deal additional damage: ";
            W_Agent ="Make friends with surrounding foes. When friend kill someone You receive experience, click on friend to make him foe again";
            E_Agent= "Teleport to any place on the map";
        }
    }

    /**
     * Set name for cheat buttons
     */
    public static class CheatButtons{
        public static String cheatGod = "Set immortal";
        public static String cheatMana = "Infinite mana";
        public static String cheatAI = "Disable AI";

        public static void setPolish(){
            cheatGod = "Nieśmiertelność";
            cheatMana = "Nieskończona mana";
            cheatAI = "Wyłącz AI";
        }

        public static void setEnglish(){
            cheatGod = "Set immortal";
            cheatMana = "Infinite mana";
            cheatAI = "Disable AI";
        }
    }

    public static class HowToPlay{
        public static String howToUseSkills ="Q,W,E keys - use skills";
        public static String howToMove="Right mouse button - move";
        public static String howToAttack ="Click right mouse button on enemy to attack";

        public static void setEnglish(){
            howToUseSkills ="Q,W,E keys - use skills";
            howToMove="Click right mouse button on map - move";
            howToAttack ="Click right mouse button on enemy to attack";
        }

        public static void setPolish(){
            howToUseSkills ="Przyciski Q,W,E - umiejętności";
            howToMove="Kliknij prawy przycisk myszy na mapie - idź";
            howToAttack ="Kliknij prawy przycisk myszy na wroga, aby zaatakować";
        }
    }

    /**
     * Sets everything to Polish language
     */
    public static void setPolish(){
        Menu.setPolish();
        PlayerPicker.setPolish();
        Skills.setPolish();
        CheatButtons.setPolish();
        HowToPlay.setPolish();
    }

    /**
     * Sets everything to English language
     */
    public static void setEnglish(){
        Menu.setEnglish();
        PlayerPicker.setEnglish();
        Skills.setEnglish();
        CheatButtons.setEnglish();
        HowToPlay.setEnglish();
    }

}

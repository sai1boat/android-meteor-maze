package com.joao024.mystery3d.mm.leveleditor;

import android.content.Context;
import android.provider.Settings;
import android.util.Log;
import android.util.Xml;

import com.badlogic.gdx.math.Vector2;
import com.joao024.mystery3d.mm.R;
import com.joao024.mystery3d.mm.game.Callable;
import com.joao024.mystery3d.mm.game.Globals;
import com.joao024.mystery3d.mm.game.LevelFactory;
import com.joao024.mystery3d.mm.game.MyGameActivity;

import org.andengine.audio.sound.Sound;
import org.andengine.entity.IEntity;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.params.BasicHttpParams;
import org.w3c.dom.Document;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xmlpull.v1.XmlSerializer;

import java.io.BufferedReader;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;


public class LevelUtils {

    /* levelString is now stored in a text file at res/values/strings.xml
     * You must wrap it in CDATA to store xml in an xml file :(
     */

    //public static String levelsString = "<?xml version='1.0' encoding='UTF-8' standalone='yes' ?><mazes><root><missleOrigin><x>64.93085</x><y>83.819885</y><angle>1.4451331</angle></missleOrigin><earthOrigin><x>1982.0305</x><y>-46.33542</y></earthOrigin><isCameraFollowsMissle>true</isCameraFollowsMissle><fileName>Procyon-815</fileName><creatorId>057dbe61</creatorId><uuid>c817f4fb-e20f-40a6-a5e9-54eaf71b05e0</uuid><isbuiltin>false</isbuiltin><wall><x>52.0</x><y>-98.0</y><rotation>-0.0086390935</rotation><width>463.01727</width><height>16.0</height><uuid>89f33927-d7df-4036-b5d7-44328099f072</uuid></wall><wall><x>-159.0</x><y>107.0</y><rotation>1.5116063</rotation><width>405.71048</width><height>16.0</height><uuid>93bf3530-1153-4976-874d-3cc490738eda</uuid></wall><wall><x>281.0</x><y>107.0</y><rotation>-1.598567</rotation><width>396.15274</width><height>16.0</height><uuid>9ec4c7e8-39d4-4ff1-aec1-552450423393</uuid></wall><wall><x>-133.0</x><y>428.0</y><rotation>-1.6741061</rotation><width>300.60272</width><height>16.0</height><uuid>ef620ade-f54a-4880-8254-629adaa6d8f1</uuid></wall><wall><x>296.36234</x><y>435.62064</y><rotation>-1.6398947</rotation><width>273.06592</width><height>16.0</height><uuid>2c04a464-920f-4433-8c56-1a0cff9377aa</uuid></wall><wall><x>432.93143</x><y>555.8428</y><rotation>-0.063658886</rotation><width>251.50945</width><height>16.0</height><uuid>4d3a12eb-b904-4cd4-99df-4325f77a4a45</uuid></wall><wall><x>-104.92627</x><y>733.64136</y><rotation>1.4942688</rotation><width>313.9188</width><height>16.0</height><uuid>cca4ebcc-f1d9-4e9b-b4df-d42a2d433c5f</uuid></wall><wall><x>396.60516</x><y>886.4179</y><rotation>0.0060974853</rotation><width>328.0061</width><height>16.0</height><uuid>6bfc72f1-5841-4079-b7fa-88210889e20f</uuid></wall><wall><x>225.51602</x><y>1000.53406</y><rotation>-1.5586512</rotation><width>247.01822</width><height>16.0</height><uuid>3ab6f1ae-5e71-4736-ba1f-5a13c7727c09</uuid></wall><wall><x>-84.71881</x><y>1007.9624</y><rotation>-1.6387765</rotation><width>235.54405</width><height>16.0</height><uuid>d415051b-c276-4456-a197-2c51838198a2</uuid></wall><wall><x>-61.954376</x><y>1268.7157</y><rotation>-1.6731769</rotation><width>293.53705</width><height>16.0</height><uuid>7c8218ba-901d-4ad3-9e5c-42b7cf797310</uuid></wall><wall><x>233.97546</x><y>1236.2397</y><rotation>-1.6590619</rotation><width>226.88322</width><height>16.0</height><uuid>de11d39b-b70d-46a7-8ef7-87019c39657a</uuid></wall><wall><x>-33.140747</x><y>1521.1576</y><rotation>-1.7048196</rotation><width>269.41605</width><height>16.0</height><uuid>316c7d82-7288-43b4-8d37-e977342d095a</uuid></wall><wall><x>251.99323</x><y>1483.1731</y><rotation>-1.6336763</rotation><width>270.53467</width><height>16.0</height><uuid>b2c924ae-6862-4bc8-b6eb-9ac77a868d06</uuid></wall><wall><x>125.78778</x><y>1628.3005</y><rotation>-0.12348201</rotation><width>284.1637</width><height>16.0</height><uuid>9b2991f1-7b71-4361-a914-b0536634bddc</uuid></wall><wall><x>568.0736</x><y>1064.706</y><rotation>1.5734917</rotation><width>371.00134</width><height>16.0</height><uuid>5fdb0191-c7b1-4ceb-9b4b-070f4c67451f</uuid></wall><wall><x>576.4008</x><y>1412.6146</y><rotation>1.5133101</rotation><width>386.02072</width><height>16.0</height><uuid>2c781857-cd25-4455-8604-4e913fd96ca5</uuid></wall><wall><x>733.23035</x><y>1594.6993</y><rotation>-0.010562987</rotation><width>284.01584</width><height>16.0</height><uuid>d842b924-8370-490c-8655-45430f1ee5fd</uuid></wall><wall><x>547.0</x><y>318.0</y><rotation>-1.5820066</rotation><width>446.028</width><height>16.0</height><uuid>7f505918-e78d-401b-bec3-379172ca0aa2</uuid></wall><wall><x>556.14343</x><y>-9.029083</y><rotation>-1.4703189</rotation><width>211.00948</width><height>16.0</height><uuid>9cecc4b9-1145-411a-82fc-f7003d65241c</uuid></wall><wall><x>739.3641</x><y>-120.05909</y><rotation>-0.014123355</rotation><width>354.0353</width><height>16.0</height><uuid>f4c1311a-a8fc-4ef8-a054-3d8719a8b80b</uuid></wall><wall><x>932.0</x><y>80.0</y><rotation>1.5305338</rotation><width>422.3423</width><height>16.0</height><uuid>0ad6acdf-8e40-40a2-b02a-fab1d77383f9</uuid></wall><wall><x>935.5646</x><y>468.1853</y><rotation>1.5874153</rotation><width>361.04987</width><height>16.0</height><uuid>6c91d603-1e29-442e-8bda-aa99afcabb87</uuid></wall><wall><x>923.90295</x><y>916.4091</y><rotation>1.6022674</rotation><width>540.2675</width><height>16.0</height><uuid>0c60e7d8-98c4-4caf-94db-7720a2143134</uuid></wall><wall><x>1351.118</x><y>1084.7539</y><rotation>1.623727</rotation><width>604.8471</width><height>16.0</height><uuid>e4b2addd-6ba4-4536-bbe7-de10dfd7fffe</uuid></wall><wall><x>911.3965</x><y>1272.3009</y><rotation>1.6164788</rotation><width>175.18275</width><height>16.0</height><uuid>4b5d3e19-aa47-4599-8a7e-319f12605379</uuid></wall><wall><x>1121.0281</x><y>1364.3359</y><rotation>0.062043823</rotation><width>413.3207</width><height>16.0</height><uuid>3f20474e-f0ea-4dad-bf67-255578a8dd4c</uuid></wall><wall><x>1369.6428</x><y>600.57837</y><rotation>-1.5626221</rotation><width>367.01227</width><height>16.0</height><uuid>ef235bf3-06a7-453d-bf30-ced82aa1033e</uuid></wall><wall><x>1368.1797</x><y>233.57745</y><rotation>-1.5869675</rotation><width>371.04852</width><height>16.0</height><uuid>3276de78-072b-4c4d-bb0f-a0bbebe4534b</uuid></wall><wall><x>1155.7158</x><y>61.07794</y><rotation>3.1206656</rotation><width>430.09418</width><height>16.0</height><uuid>10fb6477-1906-478e-a9c7-596555c87d27</uuid></wall><wall><x>1250.0</x><y>-130.0</y><rotation>-0.017239671</rotation><width>638.09485</width><height>16.0</height><uuid>7ba42a1e-fc93-4168-92cc-d37d66e0b3dd</uuid></wall><wall><x>1206.5255</x><y>1603.9777</y><rotation>0.033021025</rotation><width>666.3633</width><height>16.0</height><uuid>003c5b7a-1d32-4fec-b173-10bb160e1dfb</uuid></wall><wall><x>1786.6371</x><y>1624.9011</y><rotation>0.03821077</rotation><width>497.36304</width><height>16.0</height><uuid>573467bc-8fdb-4962-8f72-ccdeafff313d</uuid></wall><wall><x>2046.0</x><y>1508.0</y><rotation>-1.5484952</rotation><width>269.0669</width><height>16.0</height><uuid>71ce290a-e8e6-4f12-af99-0516591c526f</uuid></wall><wall><x>1821.6132</x><y>1372.9597</y><rotation>3.1761122</rotation><width>476.0378</width><height>16.0</height><uuid>bf88c6d5-4248-47ae-95d7-228e85e94f82</uuid></wall><wall><x>1596.3582</x><y>1192.8523</y><rotation>-1.5376593</rotation><width>362.19885</width><height>16.0</height><uuid>f694b1f9-6d81-4724-9b2c-7fe732d9a04e</uuid></wall><wall><x>1607.5671</x><y>839.19434</y><rotation>-1.550856</rotation><width>351.0698</width><height>16.0</height><uuid>0fae401e-e08d-433b-8ca9-4cec9b608b1f</uuid></wall><wall><x>1609.4791</x><y>486.5906</y><rotation>-1.5764301</rotation><width>355.00565</width><height>16.0</height><uuid>246b1101-2215-4851-8c4e-1e38800309ca</uuid></wall><wall><x>1605.3446</x><y>180.1297</y><rotation>-1.5898055</rotation><width>263.04752</width><height>16.0</height><uuid>d35c360d-bef2-41c2-9f47-d17d078373e8</uuid></wall><wall><x>1845.4878</x><y>53.860687</y><rotation>-0.012169785</rotation><width>493.0365</width><height>16.0</height><uuid>a852041d-d894-40f2-a6cc-0c1b74daca55</uuid></wall><wall><x>1820.4653</x><y>-135.17776</y><rotation>-0.0019563735</rotation><width>509.04813</width><height>16.0</height><uuid>b07047be-ac42-4531-a9ff-11f53ab07156</uuid></wall><wall><x>2082.9636</x><y>-48.62358</y><rotation>-1.5759026</rotation><width>190.06578</width><height>16.0</height><uuid>1a6d5ba7-79b7-4502-bfa2-b760a477be15</uuid></wall><powerup><x>87.0</x><y>462.0</y><uuid>70d41b97-fc45-4fdf-b407-5a5527f598ea</uuid></powerup><powerup><x>640.0</x><y>812.0</y><uuid>697c03ca-f3b1-4f33-8d9f-d9b503266cac</uuid></powerup><powerup><x>817.3941</x><y>1323.6228</y><uuid>23ffaeb2-6c2e-4598-b5d3-a5cf4e571813</uuid></powerup><powerup><x>1070.0</x><y>1478.0</y><uuid>a6d9fa7a-37a1-443b-b2e0-a2b5c6430d57</uuid></powerup><powerup><x>1452.0</x><y>1376.0</y><uuid>9b5a3dac-a71a-49ea-b8ab-5d22a56a5e1d</uuid></powerup><powerup><x>1455.4741</x><y>101.92234</y><uuid>e78f7a77-44f3-4d45-a471-aa7d14451f0d</uuid></powerup><powerup><x>1540.0</x><y>-27.0</y><uuid>137ab6c1-b8e4-432e-a8e8-e82839e1b916</uuid></powerup><powerup><x>1690.0</x><y>-62.0</y><uuid>ada40bff-0674-42cf-bdac-57dbaf3959d8</uuid></powerup><powerup><x>353.0</x><y>722.0</y><uuid>b5b1d717-845f-4445-9b73-711946ece983</uuid></powerup><powerup><x>828.0</x><y>1144.0</y><uuid>b4344b07-a0bf-4de2-9521-898824d69483</uuid></powerup><powerup><x>1444.0</x><y>1020.0</y><uuid>289b4120-f039-4beb-b7cc-2e31f1b08fe1</uuid></powerup><powerup><x>1425.3312</x><y>585.9963</y><uuid>3e617622-00a5-41fb-b763-00528b7b8eee</uuid></powerup></root><root><missleOrigin><x>0.0</x><y>0.0</y><angle>0.0</angle></missleOrigin><earthOrigin><x>2931.391</x><y>2565.364</y></earthOrigin><isCameraFollowsMissle>true</isCameraFollowsMissle><fileName>Matar-152</fileName><creatorId>057dbe61</creatorId><uuid>82a4ce20-3b79-43a3-b7cf-da7c341828d0</uuid><isbuiltin>false</isbuiltin><wall><x>-87.85718</x><y>9.798157</y><rotation>-1.5411949</rotation><width>311.57983</width><height>16.0</height><uuid>00bfd80d-bd12-4269-b507-2fe74f720781</uuid></wall><wall><x>135.0</x><y>160.0</y><rotation>-0.010548132</rotation><width>474.02637</width><height>16.0</height><uuid>d918afed-c09f-4195-89da-74a445a62649</uuid></wall><wall><x>131.0</x><y>-144.0</y><rotation>-0.028769039</rotation><width>417.17264</width><height>16.0</height><uuid>6d24bf72-607b-4943-9aee-b8ee8ff48a96</uuid></wall><wall><x>366.0</x><y>104.0</y><rotation>-1.5142529</rotation><width>106.16968</width><height>16.0</height><uuid>cf58453a-c441-4e10-8fbd-f00a6507094e</uuid></wall><wall><x>409.9285</x><y>53.771088</y><rotation>0.0</rotation><width>100.0</width><height>16.0</height><uuid>c6921ea1-ff1e-4de1-a707-96ec25b33e35</uuid></wall><wall><x>462.7857</x><y>103.75763</y><rotation>1.5265774</rotation><width>113.110565</width><height>16.0</height><uuid>fce01b77-e132-4e8c-9ba6-1224d8ba75ee</uuid></wall><wall><x>338.85693</x><y>-105.41969</y><rotation>1.4141945</rotation><width>76.941536</width><height>16.0</height><uuid>4359d81e-5258-4202-9ad9-d6b5a5531902</uuid></wall><wall><x>400.2856</x><y>-75.49103</y><rotation>0.0102037275</rotation><width>98.005104</width><height>16.0</height><uuid>feea657f-8785-478d-91e4-18f6b018f2fa</uuid></wall><wall><x>449.92377</x><y>-115.90069</y><rotation>-1.3057535</rotation><width>72.53275</width><height>16.0</height><uuid>6321fa49-d52c-462a-a39c-b614cceb8a80</uuid></wall><wall><x>626.073</x><y>150.59787</y><rotation>-0.0131140025</rotation><width>305.0262</width><height>16.0</height><uuid>bafb47d3-8fb7-4e3b-a350-d00ee1919775</uuid></wall><wall><x>605.3572</x><y>-152.19733</y><rotation>0.0130711505</rotation><width>306.02615</width><height>16.0</height><uuid>e0e720cb-44e4-4499-a338-53951724ebcb</uuid></wall><wall><x>907.028</x><y>188.31131</y><rotation>0.30008918</rotation><width>277.39682</width><height>16.0</height><uuid>21545749-563b-4ca0-ba56-c330ef1e809a</uuid></wall><wall><x>956.64276</x><y>-97.59722</y><rotation>0.26013786</rotation><width>415.9964</width><height>16.0</height><uuid>53b669a7-04df-4211-8347-2ff2e35f482f</uuid></wall><wall><x>1063.3572</x><y>199.91833</y><rotation>-0.84781694</rotation><width>90.68627</width><height>16.0</height><uuid>c4d02a63-3745-47e4-a518-bbfd7788eeaa</uuid></wall><wall><x>1122.9634</x><y>178.95563</y><rotation>0.28757584</rotation><width>74.04053</width><height>16.0</height><uuid>45006ad8-f5bb-4b2f-a69a-91a0f66208cd</uuid></wall><wall><x>1165.8572</x><y>229.4111</y><rotation>-1.7561443</rotation><width>97.67292</width><height>16.0</height><uuid>28f67888-c27c-46a3-830a-f1b9dab2378c</uuid></wall><wall><x>1151.5715</x><y>-7.379898</y><rotation>-1.7083831</rotation><width>65.62012</width><height>16.0</height><uuid>09ccb539-6586-4e11-823b-2e1652b1ac48</uuid></wall><wall><x>1192.9849</x><y>38.039383</y><rotation>0.45333868</rotation><width>86.764046</width><height>16.0</height><uuid>8f5ee249-5c8f-478f-ba15-a11bc1e5138d</uuid></wall><wall><x>1270.1357</x><y>33.761383</y><rotation>-0.4464819</rotation><width>104.21612</width><height>16.0</height><uuid>abd176d5-4a29-48d6-b636-6aecdd1507d3</uuid></wall><wall><x>1287.3573</x><y>354.47354</y><rotation>0.6231101</rotation><width>284.45914</width><height>16.0</height><uuid>76a3f15f-1d17-469f-a1f2-c14277a4b793</uuid></wall><wall><x>1463.0714</x><y>126.92937</y><rotation>0.62149715</rotation><width>372.69022</width><height>16.0</height><uuid>d2bfd05c-d491-4e8e-9e4b-169d41713771</uuid></wall><wall><x>1433.4285</x><y>528.66486</y><rotation>1.233422</rotation><width>202.41048</width><height>16.0</height><uuid>a245f830-8aa4-448e-aed7-b60ddb08ecbe</uuid></wall><wall><x>1679.1427</x><y>418.4162</y><rotation>1.2250503</rotation><width>395.39853</width><height>16.0</height><uuid>87d068fd-d8e7-4f74-a244-661b3436b9b4</uuid></wall><wall><x>1484.1069</x><y>777.0327</y><rotation>1.4516226</rotation><width>311.20734</width><height>16.0</height><uuid>77076772-289b-4542-afac-310e03f85130</uuid></wall><wall><x>1754.6265</x><y>777.42096</y><rotation>1.5165639</rotation><width>350.51532</width><height>16.0</height><uuid>7edd80a3-dc69-4d5c-ae21-c7c207dea948</uuid></wall><wall><x>1776.5085</x><y>1137.3892</y><rotation>1.5038723</rotation><width>373.83685</width><height>16.0</height><uuid>aea00014-8600-44cc-92e7-5db9ae400adb</uuid></wall><wall><x>1534.0</x><y>1204.0</y><rotation>1.4651837</rotation><width>284.58566</width><height>16.0</height><uuid>8fbd034d-4caf-4ea4-9dea-9c0600021824</uuid></wall><wall><x>1556.298</x><y>1519.6556</y><rotation>1.5245864</rotation><width>346.36975</width><height>16.0</height><uuid>e9373396-30a1-4930-a12b-9e5daccc9a69</uuid></wall><wall><x>1787.9048</x><y>1510.6514</y><rotation>1.573463</rotation><width>375.00134</width><height>16.0</height><uuid>005f2b62-ba42-4180-b67e-db0000b53cae</uuid></wall><wall><x>1741.8582</x><y>1794.658</y><rotation>0.011235482</rotation><width>89.005615</width><height>16.0</height><uuid>aa98efd5-f899-40f0-8485-13e76a581ff5</uuid></wall><wall><x>1565.7863</x><y>1794.658</y><rotation>1.5513813</rotation><width>206.03883</width><height>16.0</height><uuid>260bc0c8-bc91-4186-91f6-36236fab861d</uuid></wall><wall><x>1798.786</x><y>1923.6179</y><rotation>1.5379614</rotation><width>274.14777</width><height>16.0</height><uuid>45a01af9-fd68-4f69-bc5a-071ad550d63f</uuid></wall><wall><x>1567.3564</x><y>1995.4347</y><rotation>1.5707964</rotation><width>197.0</width><height>16.0</height><uuid>1b40c2f5-86e8-4cc3-811c-59d7e4f0395f</uuid></wall><wall><x>1809.256</x><y>2138.205</y><rotation>1.501724</rotation><width>159.38005</width><height>16.0</height><uuid>1eb00555-a9a5-45c6-94ac-1ea3cba23bfc</uuid></wall><wall><x>1565.0</x><y>978.0</y><rotation>0.7474417</rotation><width>167.7051</width><height>16.0</height><uuid>92ba535b-3a99-4a25-ae50-9908a3194d52</uuid></wall><wall><x>1568.9441</x><y>1052.5112</y><rotation>-0.0893743</rotation><width>113.03982</width><height>16.0</height><uuid>24ec291a-e41f-43e3-b81f-45fb9a82a1e0</uuid></wall><wall><x>1747.0</x><y>1734.0</y><rotation>-0.73427993</rotation><width>152.2268</width><height>16.0</height><uuid>9c1e5c3e-f52b-4357-8d93-51fcd96eada1</uuid></wall><wall><x>1630.5881</x><y>2249.578</y><rotation>1.196752</rotation><width>344.84344</width><height>16.0</height><uuid>0c01dd43-5fa0-419f-9489-58ab90ff4195</uuid></wall><wall><x>1856.664</x><y>2302.008</y><rotation>1.1207225</rotation><width>197.68661</width><height>16.0</height><uuid>d21f15e1-49ec-43fd-981e-6c321001dd72</uuid></wall><wall><x>1687.0</x><y>2534.0</y><rotation>-1.5400368</rotation><width>260.12305</width><height>16.0</height><uuid>a6866978-b6c5-4e97-a1d1-f6ed14fe63b2</uuid></wall><wall><x>1704.873</x><y>2686.1448</y><rotation>0.8139618</rotation><width>74.27651</width><height>16.0</height><uuid>ef679520-2edc-4e67-aa76-beef005e70d6</uuid></wall><wall><x>2072.6475</x><y>2701.2842</y><rotation>-0.027252935</rotation><width>697.2589</width><height>16.0</height><uuid>1ac76a3c-a15e-4ade-8415-503be54ca72c</uuid></wall><wall><x>1895.2322</x><y>2415.5442</y><rotation>-1.3952042</rotation><width>62.968246</width><height>16.0</height><uuid>e50cf707-191d-41b7-838b-2753ddfb2c60</uuid></wall><wall><x>1899.0</x><y>2459.0</y><rotation>0.8224183</rotation><width>38.209946</width><height>16.0</height><uuid>1db6bc4c-0110-4edc-8c18-0eb925d2c041</uuid></wall><wall><x>2162.52</x><y>2460.7053</y><rotation>-0.038508795</rotation><width>515.25916</width><height>16.0</height><uuid>64c1719c-2bc6-4229-98ca-3454b764e565</uuid></wall><wall><x>2438.1611</x><y>2662.3088</y><rotation>-0.9723774</rotation><width>79.881165</width><height>16.0</height><uuid>1f636c1a-ce86-450f-82b3-c35e09be3011</uuid></wall><wall><x>2494.939</x><y>2631.6323</y><rotation>-0.024994794</rotation><width>80.024994</width><height>16.0</height><uuid>d09ed5f0-4e57-4cc4-89ac-414034bcb310</uuid></wall><wall><x>2562.448</x><y>2659.6008</y><rotation>0.7617805</rotation><width>89.827614</width><height>16.0</height><uuid>a5509c0a-2864-4931-873a-04b6dc6ae1a5</uuid></wall><wall><x>2433.0</x><y>2468.0</y><rotation>0.9780989</rotation><width>59.07622</width><height>16.0</height><uuid>aa083d29-b7c9-4bd4-986c-df5c68dac245</uuid></wall><wall><x>2499.0</x><y>2483.0</y><rotation>-0.042709056</rotation><width>117.10679</width><height>16.0</height><uuid>5c7cad0b-656e-4b95-a8ed-08b8395d0536</uuid></wall><wall><x>2573.8767</x><y>2463.9417</y><rotation>-0.7157436</rotation><width>60.959003</width><height>16.0</height><uuid>20bd86c8-d776-4057-929a-d1abead6fa86</uuid></wall><wall><x>2780.3071</x><y>2682.4949</y><rotation>-0.028939286</rotation><width>380.15918</width><height>16.0</height><uuid>8bd09b7c-750b-411c-82db-be922d68762d</uuid></wall><wall><x>2773.6475</x><y>2450.355</y><rotation>0.016347317</rotation><width>367.04904</width><height>16.0</height><uuid>3dd234a2-af2f-42cc-a281-3b2a81982151</uuid></wall><wall><x>2984.747</x><y>2479.4375</y><rotation>0.71654165</rotation><width>82.219215</width><height>16.0</height><uuid>c2c629c9-12a1-4278-9536-3a5bf7a743cd</uuid></wall><wall><x>2993.1804</x><y>2651.6008</y><rotation>-0.7375701</rotation><width>79.17702</width><height>16.0</height><uuid>0d9a7354-d80f-4b4d-b557-da9288a1564d</uuid></wall><wall><x>3016.6057</x><y>2563.8694</y><rotation>-1.5453781</rotation><width>118.03813</width><height>16.0</height><uuid>2fb44044-0c41-4d7c-a5f2-e18af76328e4</uuid></wall><powerup><x>751.0</x><y>-92.0</y><uuid>e6820e26-6372-47df-bdb5-b5919335ee4a</uuid></powerup><powerup><x>1441.0</x><y>410.0</y><uuid>ef73b2dc-4aa0-495a-94b5-3d41b6627e75</uuid></powerup><powerup><x>1662.0</x><y>1039.0</y><uuid>7ffc4757-9515-4646-a0cf-e1d5fd4b8299</uuid></powerup><powerup><x>1656.0</x><y>1789.0</y><uuid>8e26bfea-062e-4e9f-9acd-3b6fbbc758fb</uuid></powerup><powerup><x>1991.0</x><y>2663.0</y><uuid>14355e4c-5913-41b2-8f3b-d2f941a6d0c8</uuid></powerup><powerup><x>2485.0</x><y>2522.0</y><uuid>af9247dc-d683-4a90-addd-7af19d1410db</uuid></powerup><powerup><x>1047.0</x><y>26.0</y><uuid>9e424931-aa00-418a-95a0-821179fa29f6</uuid></powerup><powerup><x>1352.0</x><y>217.0</y><uuid>83f5dfa9-ebc7-4147-942e-f0142c9b6aa4</uuid></powerup><powerup><x>1689.0</x><y>719.0</y><uuid>8d4cf062-dece-4afa-88a4-97ab77b6d6cf</uuid></powerup><powerup><x>1613.0</x><y>1429.0</y><uuid>a99a7691-314d-4f6f-b2a9-9e0e629c34b6</uuid></powerup><powerup><x>1625.0</x><y>1618.0</y><uuid>f9e4c2ef-a929-46cf-b99e-23d2eea785a5</uuid></powerup><powerup><x>1709.0</x><y>2160.0</y><uuid>12f83d02-b852-4ea7-87ba-45149dd3d7fd</uuid></powerup><powerup><x>1788.0</x><y>2373.0</y><uuid>325ea8e5-53d4-47f0-9383-2ce3bceffe8f</uuid></powerup><powerup><x>2245.0</x><y>2573.0</y><uuid>0d1e13f5-3bd2-4e4d-bc18-a8962b414ebe</uuid></powerup></root><root><missleOrigin><x>0.0</x><y>0.0</y><angle>0.0</angle></missleOrigin><earthOrigin><x>1479.0</x><y>-199.0</y></earthOrigin><isCameraFollowsMissle>true</isCameraFollowsMissle><fileName>Azha-234</fileName><creatorId>057dbe61</creatorId><uuid>cebe02b2-5d5e-4b77-bdbd-1ad586f8a2b4</uuid><isbuiltin>false</isbuiltin><wall><x>-199.0</x><y>142.0</y><rotation>-2.4844072</rotation><width>204.61916</width><height>16.0</height><uuid>93984130-e42a-4414-885c-98383f3c9c1e</uuid></wall><wall><x>-271.0</x><y>32.0</y><rotation>-1.6517004</rotation><width>111.364265</width><height>16.0</height><uuid>f3061036-59a6-4079-9eeb-0af5efea88bd</uuid></wall><wall><x>-192.0</x><y>-92.0</y><rotation>-0.6889244</rotation><width>220.22716</width><height>16.0</height><uuid>6fc61005-25ff-4982-a2a4-ad5acd14290b</uuid></wall><wall><x>216.49985</x><y>190.54349</y><rotation>-0.032389257</rotation><width>679.3563</width><height>16.0</height><uuid>f3fb5f92-e0b8-4c81-b51e-1655ca2470f7</uuid></wall><wall><x>214.07141</x><y>-153.0124</y><rotation>0.012383268</rotation><width>646.04956</width><height>16.0</height><uuid>7cc65056-fee7-4642-bcb5-df45176333b5</uuid></wall><wall><x>862.74805</x><y>74.43323</y><rotation>-0.32852027</rotation><width>653.974</width><height>16.0</height><uuid>4e02df8a-1da6-44e9-b323-1ac2d917e872</uuid></wall><wall><x>837.4999</x><y>-246.45189</y><rotation>-0.30635187</rotation><width>636.64197</width><height>16.0</height><uuid>f359e938-2c90-4fe8-8086-47b98969900b</uuid></wall><wall><x>1374.9109</x><y>-39.54187</y><rotation>-0.04154088</rotation><width>409.35315</width><height>16.0</height><uuid>695aeadc-4ec5-403d-834f-31a246866172</uuid></wall><wall><x>1584.2141</x><y>-201.80899</y><rotation>-1.5801419</rotation><width>321.014</width><height>16.0</height><uuid>39e0ad7c-9779-4ae9-a564-429330850f3c</uuid></wall><wall><x>1359.5713</x><y>-348.54816</y><rotation>-0.03407771</rotation><width>440.2556</width><height>16.0</height><uuid>2747b577-76eb-446f-84df-1ac71da5905b</uuid></wall><powerup><x>873.0</x><y>-83.0</y><uuid>5a547239-6643-49c3-9eca-302b3e9fadfa</uuid></powerup><powerup><x>1141.3433</x><y>-153.60272</y><uuid>507b9c93-d154-4d8c-ab2c-b5914e212633</uuid></powerup><powerup><x>127.0</x><y>38.0</y><uuid>6916ab35-ca94-4c03-95e9-a6883abc5a68</uuid></powerup><powerup><x>375.89157</x><y>104.39526</y><uuid>160a9480-9da0-41cb-b184-715eafb7b959</uuid></powerup><powerup><x>560.0</x><y>65.0</y><uuid>1ccb5ac1-c2b4-4777-aee0-c59a23a7a9b1</uuid></powerup><powerup><x>724.0</x><y>-12.0</y><uuid>0d9959b5-fc4d-4de9-afc6-424755db9d0e</uuid></powerup><powerup><x>1009.0</x><y>-118.0</y><uuid>142904c1-e0bf-4807-a804-80cda35ebb53</uuid></powerup><powerup><x>1315.0</x><y>-190.0</y><uuid>41874163-9290-4cab-90bd-2f8a8371043f</uuid></powerup><powerup><x>211.0</x><y>57.0</y><uuid>e3f6be74-19ad-481a-b63c-3548dc151d6e</uuid></powerup><powerup><x>279.0</x><y>80.0</y><uuid>895f2b8b-e1f1-4b67-93d1-5d8e7b51a7f6</uuid></powerup><powerup><x>476.0</x><y>93.0</y><uuid>e48e579a-ec8d-4b15-a3b1-10540e6ecdba</uuid></powerup><powerup><x>638.0</x><y>30.0</y><uuid>514e5a5f-5387-495f-8013-77f06ac6a55a</uuid></powerup><powerup><x>800.0</x><y>-57.0</y><uuid>c0294c0d-c4e9-4457-b1bf-5dc19f85d3e2</uuid></powerup><powerup><x>938.0</x><y>-103.0</y><uuid>92b1c97a-2577-4701-93db-1703d9a0aaeb</uuid></powerup><powerup><x>1074.0</x><y>-120.0</y><uuid>a94016aa-ec90-49d2-90bc-cdb7f4ad3536</uuid></powerup><powerup><x>1220.0</x><y>-174.0</y><uuid>bddde96b-021e-4644-8b73-c6d609e3e906</uuid></powerup></root><root><missleOrigin><x>0.0</x><y>0.0</y><angle>0.0</angle></missleOrigin><earthOrigin><x>1980.0</x><y>4247.0</y></earthOrigin><isCameraFollowsMissle>true</isCameraFollowsMissle><fileName>Alrisha-440</fileName><creatorId>057dbe61</creatorId><uuid>816670da-5090-4239-a401-ec58436081b7</uuid><isbuiltin>false</isbuiltin><wall><x>-122.979004</x><y>-37.75043</y><rotation>-1.5128409</rotation><width>293.49277</width><height>24.0</height><uuid>b29caf8c-b242-4793-8be9-5741096e40fe</uuid></wall><wall><x>-36.241577</x><y>124.18269</y><rotation>0.041836042</rotation><width>215.1883</width><height>24.0</height><uuid>185a191b-e2f6-4110-a138-b859131a82b9</uuid></wall><wall><x>165.29553</x><y>75.38098</y><rotation>-0.49865276</rotation><width>230.0087</width><height>24.0</height><uuid>4266c5db-5ed9-475e-a7a9-6d7b7e2337dd</uuid></wall><wall><x>-27.057587</x><y>-191.36507</y><rotation>0.056933224</rotation><width>193.31322</width><height>24.0</height><uuid>112e0a81-ffe8-4d72-b8d6-b94b9ddbf79e</uuid></wall><wall><x>194.37805</x><y>-254.03099</y><rotation>-0.48367575</rotation><width>290.29984</width><height>24.0</height><uuid>de7c738f-c083-47ee-b930-4aba8976ba15</uuid></wall><wall><x>410.0</x><y>22.0</y><rotation>0.0</rotation><width>296.0</width><height>24.0</height><uuid>5f2205eb-8a38-4a25-a4dc-c83ac3c55001</uuid></wall><wall><x>563.1718</x><y>-315.64972</y><rotation>0.024237677</rotation><width>495.14545</width><height>24.0</height><uuid>b4d4e790-d53f-477f-8e90-2e32901dba8a</uuid></wall><wall><x>915.7342</x><y>-199.19098</y><rotation>0.79428685</rotation><width>318.21063</width><height>24.0</height><uuid>43dd98ee-58fa-43f1-8112-372317cf66a5</uuid></wall><wall><x>1032.0317</x><y>145.4687</y><rotation>1.549613</rotation><width>472.10593</width><height>24.0</height><uuid>6a380e00-5669-4b5e-b3cd-746d0e90d1d1</uuid></wall><wall><x>752.0</x><y>385.0</y><rotation>3.0876882</rotation><width>556.8088</width><height>24.0</height><uuid>ee8b732e-bcd6-4b60-85e8-08584aa83937</uuid></wall><wall><x>245.62543</x><y>682.7832</y><rotation>1.5471121</rotation><width>591.1658</width><height>24.0</height><uuid>58740a14-5e01-4923-bae2-937270bfaa69</uuid></wall><wall><x>484.91898</x><y>702.1932</y><rotation>1.5698146</rotation><width>590.56665</width><height>24.0</height><uuid>cf8f7028-202f-4ba7-9359-99ccf9ef2b7f</uuid></wall><wall><x>279.0</x><y>1117.0</y><rotation>1.3764551</rotation><width>321.0436</width><height>24.0</height><uuid>144fdd17-677e-4f52-a9c1-733304224b6a</uuid></wall><wall><x>534.26355</x><y>1166.9829</y><rotation>1.2992635</rotation><width>365.38745</width><height>24.0</height><uuid>e8891f4c-f575-42e3-9c86-22ecfedc8a91</uuid></wall><wall><x>403.29282</x><y>1501.0071</y><rotation>1.1895221</rotation><width>483.7365</width><height>24.0</height><uuid>2a900abd-c8ee-4e99-8127-57552dafabcc</uuid></wall><wall><x>674.73267</x><y>1542.6433</y><rotation>1.1443549</rotation><width>444.83817</width><height>24.0</height><uuid>42b2f0fc-a9a4-47c3-acba-4a72b64616f4</uuid></wall><wall><x>668.0</x><y>1981.0</y><rotation>0.9722054</rotation><width>654.8603</width><height>24.0</height><uuid>ea05b577-dfce-449e-8e89-21d98dc7d87a</uuid></wall><wall><x>875.2494</x><y>1855.9733</y><rotation>0.79428685</rotation><width>318.21063</width><height>24.0</height><uuid>174604d4-9030-421d-908a-972903aa06bd</uuid></wall><wall><x>1176.003</x><y>2253.237</y><rotation>0.023983406</rotation><width>667.1919</width><height>24.0</height><uuid>43f9262c-22e0-45fd-a257-b6d105e89279</uuid></wall><wall><x>1241.3666</x><y>1973.9044</y><rotation>0.036452178</rotation><width>521.3463</width><height>24.0</height><uuid>5c55554b-f02f-4502-a103-28d858934bb5</uuid></wall><wall><x>1578.4297</x><y>2305.012</y><rotation>0.62657404</rotation><width>165.42369</width><height>24.0</height><uuid>74570921-4827-4304-8544-e734e2186d3c</uuid></wall><wall><x>1632.1956</x><y>2032.6884</y><rotation>0.3664318</rotation><width>290.27057</width><height>24.0</height><uuid>5bd5f424-e6df-41e7-89c1-fff1565e25a3</uuid></wall><wall><x>1818.9712</x><y>2180.7566</y><rotation>1.0439917</rotation><width>226.74214</width><height>24.0</height><uuid>8deeeafb-b84a-4efd-b1e3-1558fbeea28b</uuid></wall><wall><x>1929.3478</x><y>2396.1343</y><rotation>1.1391783</rotation><width>265.33374</width><height>24.0</height><uuid>a41fcb06-698a-46f0-90da-e078c325c2c2</uuid></wall><wall><x>1731.113</x><y>2481.641</y><rotation>0.9603357</rotation><width>308.76852</width><height>24.0</height><uuid>c84541eb-116e-4379-9398-ff5dd5682eeb</uuid></wall><wall><x>1839.5565</x><y>2770.753</y><rotation>1.4681283</rotation><width>331.7469</width><height>24.0</height><uuid>72e966c5-bf23-4ddd-bd16-ef2e710a98c5</uuid></wall><wall><x>1802.0552</x><y>3070.6577</y><rotation>1.9801167</rotation><width>296.49283</width><height>24.0</height><uuid>9f0ab24c-1d4d-4bda-ab60-df1f96081b26</uuid></wall><wall><x>1644.291</x><y>3249.432</y><rotation>2.6992185</rotation><width>231.26175</width><height>24.0</height><uuid>62c6b67b-323b-4115-91bc-260ac4ada49a</uuid></wall><wall><x>2035.6107</x><y>2747.5608</y><rotation>1.3532637</rotation><width>477.2473</width><height>24.0</height><uuid>ae487f7f-c315-425a-ae7c-e43c65f9ec41</uuid></wall><wall><x>2027.0481</x><y>3138.0173</y><rotation>1.9390038</rotation><width>347.27655</width><height>24.0</height><uuid>3c7bdc82-18dc-4f8b-8f1d-7140532ca3af</uuid></wall><wall><x>1602.0</x><y>3432.0</y><rotation>1.1943855</rotation><width>272.04596</width><height>24.0</height><uuid>8ee0a214-f714-4aa5-87b8-c5543d7cc213</uuid></wall><wall><x>1756.2228</x><y>3738.4526</y><rotation>1.0406518</rotation><width>417.27808</width><height>24.0</height><uuid>991a6bb7-dc09-457a-b9d6-6d613178f253</uuid></wall><wall><x>1989.79</x><y>3382.114</y><rotation>1.3372967</rotation><width>190.16046</width><height>24.0</height><uuid>042dd534-17d7-4a45-a88c-f615b2967912</uuid></wall><wall><x>2015.7875</x><y>3555.028</y><rotation>1.5149539</rotation><width>161.25136</width><height>24.0</height><uuid>e0fe4d21-fb9d-4270-b34f-dac67e5372fd</uuid></wall><wall><x>2099.9736</x><y>3758.4683</y><rotation>1.0199777</rotation><width>313.34485</width><height>24.0</height><uuid>14a35e40-1d87-4172-b3bc-e5725079a3a6</uuid></wall><wall><x>1871.5948</x><y>4108.7266</y><rotation>1.5293692</rotation><width>386.33145</width><height>24.0</height><uuid>86ce774d-46ff-49cf-91ab-ededf97419a9</uuid></wall><wall><x>2154.997</x><y>4033.549</y><rotation>1.7386376</rotation><width>305.29004</width><height>24.0</height><uuid>dd9db857-4727-401c-9c60-cfed88669af6</uuid></wall><wall><x>270.51147</x><y>323.2073</y><rotation>-1.1629164</rotation><width>151.63443</width><height>24.0</height><uuid>7d349462-798d-4154-9d4b-4c97aef67685</uuid></wall><wall><x>422.4155</x><y>140.75519</y><rotation>-0.756055</rotation><width>338.01776</width><height>24.0</height><uuid>0c94325d-28fc-43aa-b064-cb8ffe234a51</uuid></wall><wall><x>1993.0</x><y>4298.0</y><rotation>0.02458521</rotation><width>244.07376</width><height>24.0</height><uuid>641c33a3-b42c-4499-b0da-6683153f5d95</uuid></wall><wall><x>2114.9302</x><y>4231.8213</y><rotation>-1.2862779</rotation><width>110.440025</width><height>24.0</height><uuid>aa4bba7a-16ce-4ebf-895e-d025753b87fa</uuid></wall><powerup><x>306.0</x><y>-130.0</y><uuid>1bde9bbb-9923-48a0-96da-40314133c105</uuid></powerup><powerup><x>651.0</x><y>-134.0</y><uuid>a377cae0-b6b6-4b0c-bc1e-5880fd66f5d0</uuid></powerup><powerup><x>810.0</x><y>222.0</y><uuid>9736198b-86a0-4a1f-be31-6ac8c43793ab</uuid></powerup><powerup><x>457.0</x><y>213.0</y><uuid>f0f95d98-d64b-4002-a10b-c870d3913caa</uuid></powerup><powerup><x>361.0</x><y>661.0</y><uuid>770ba22e-6ab7-4b2c-b534-305c8e165cb7</uuid></powerup><powerup><x>360.0</x><y>1025.0</y><uuid>ee623287-d0c3-49c3-83fb-9d0e4ab6c439</uuid></powerup><powerup><x>476.0</x><y>1350.0</y><uuid>19cdad40-9650-48e8-acbe-92ecf8e994c0</uuid></powerup><powerup><x>604.0</x><y>1671.0</y><uuid>d273c7de-0299-419d-9f3b-e4646ccdc933</uuid></powerup><powerup><x>890.0</x><y>2096.0</y><uuid>8c46b4bd-517f-40c0-bf57-6311659ef0ee</uuid></powerup><powerup><x>1449.0</x><y>2048.0</y><uuid>3bb03ebf-0ade-4a9c-885c-df6fe31a3439</uuid></powerup><powerup><x>1752.0</x><y>2297.0</y><uuid>d6d1ed8d-8756-4655-b5c2-bbb2f68178b3</uuid></powerup><powerup><x>1896.0</x><y>2561.0</y><uuid>2d3ec71f-6968-4a10-a8a2-2c6aa3a4a55f</uuid></powerup><powerup><x>1962.0</x><y>2955.0</y><uuid>6ebfeb1d-a5b9-4c68-bdc1-1e895a7c3a21</uuid></powerup><powerup><x>1829.0</x><y>3400.0</y><uuid>4c793ab6-1983-4342-9ade-8f889a88ba81</uuid></powerup><powerup><x>1888.0</x><y>3687.0</y><uuid>f782b716-dce3-47b4-a156-b2cc56c5f1cf</uuid></powerup></root><root><missleOrigin><x>0.0</x><y>0.0</y><angle>0.0</angle></missleOrigin><earthOrigin><x>5409.0</x><y>-235.0</y></earthOrigin><isCameraFollowsMissle>true</isCameraFollowsMissle><fileName>Thabit-237</fileName><creatorId>057dbe61</creatorId><uuid>a2755402-6f1e-4a05-b382-08c9ec7bff07</uuid><isbuiltin>false</isbuiltin><wall><x>-134.0</x><y>-56.0</y><rotation>-0.98279375</rotation><width>234.36084</width><height>24.0</height><uuid>4d45ce42-6c2c-4225-b54b-6be235ac6178</uuid></wall><wall><x>-90.0</x><y>117.0</y><rotation>0.6572385</rotation><width>276.6261</width><height>24.0</height><uuid>702e2b2a-1abb-4e7c-93c8-ebd5805545d3</uuid></wall><wall><x>83.0</x><y>-54.0</y><rotation>0.5702135</rotation><width>374.2045</width><height>24.0</height><uuid>0add8767-9167-4332-ad28-e807c137319a</uuid></wall><wall><x>-3.0</x><y>356.0</y><rotation>1.7050325</rotation><width>313.8232</width><height>24.0</height><uuid>6a191cb5-3e78-453c-8be1-5e29d5db1ddd</uuid></wall><wall><x>318.0</x><y>522.0</y><rotation>0.07202527</rotation><width>694.8014</width><height>24.0</height><uuid>d9f7872f-41d2-4028-91f5-4f5a661383f5</uuid></wall><wall><x>672.0</x><y>377.0</y><rotation>-1.5282688</rotation><width>329.29773</width><height>24.0</height><uuid>f0c29990-7c1e-42c4-9a2b-cdfc37385aef</uuid></wall><wall><x>565.0</x><y>203.0</y><rotation>-3.039386</rotation><width>235.22755</width><height>24.0</height><uuid>229d5cde-5871-4f6c-aef0-8bc5d35cf5f1</uuid></wall><wall><x>669.0</x><y>-12.0</y><rotation>-0.7302981</rotation><width>590.6234</width><height>24.0</height><uuid>c171102f-7841-40a2-8c9b-7117f4cd55e6</uuid></wall><wall><x>276.0</x><y>-315.0</y><rotation>-2.9832249</rotation><width>1236.4733</width><height>24.0</height><uuid>30b0d8e2-a468-43f3-9d2c-45badaf95123</uuid></wall><wall><x>-482.0</x><y>-188.0</y><rotation>2.1341126</rotation><width>561.8051</width><height>24.0</height><uuid>2f3b0429-2ee1-4d57-a9f7-759c93280509</uuid></wall><wall><x>-480.0</x><y>248.0</y><rotation>0.8713924</rotation><width>493.97165</width><height>24.0</height><uuid>f05de9da-f35b-47a6-9403-8a4e1a4680e7</uuid></wall><wall><x>-351.0</x><y>662.0</y><rotation>1.6307244</rotation><width>500.8992</width><height>24.0</height><uuid>82346252-41d6-4866-a8c7-acd27c323765</uuid></wall><wall><x>262.0</x><y>838.0</y><rotation>-0.099435315</rotation><width>1279.3193</width><height>24.0</height><uuid>9187f8ad-52bc-483e-a39a-e091f0d4541d</uuid></wall><wall><x>548.56323</x><y>-24.10675</y><rotation>-1.8859024</rotation><width>190.37332</width><height>24.0</height><uuid>02762902-7548-4d43-91d8-565d865fa1d4</uuid></wall><wall><x>645.4381</x><y>-119.8515</y><rotation>0.021894312</rotation><width>274.06567</width><height>24.0</height><uuid>06808195-ddfb-4312-991d-1c2b13de58ed</uuid></wall><wall><x>991.18744</x><y>483.7448</y><rotation>-1.2378446</rotation><width>621.1103</width><height>24.0</height><uuid>f043d443-4b1f-4107-a5da-38d5a9378e29</uuid></wall><wall><x>1044.3125</x><y>148.63829</y><rotation>-2.3235974</rotation><width>130.1768</width><height>24.0</height><uuid>1fcc416b-eb35-4f33-b4a7-7a56ca20c707</uuid></wall><wall><x>1350.5625</x><y>-256.6809</y><rotation>-0.7722864</rotation><width>1024.6858</width><height>24.0</height><uuid>f517d53e-b1fb-4572-b7f4-171964fac733</uuid></wall><wall><x>1278.6875</x><y>-563.3191</y><rotation>-0.73901236</rotation><width>1067.4652</width><height>24.0</height><uuid>f632be24-ee61-4ce2-b2b3-22c9389c3f08</uuid></wall><wall><x>1801.0</x><y>-625.0</y><rotation>-0.010988569</rotation><width>182.01099</width><height>24.0</height><uuid>797c1169-06b1-4cc4-af10-014a30cdaf3a</uuid></wall><wall><x>1970.0</x><y>-600.0</y><rotation>0.40341905</rotation><width>178.31433</width><height>24.0</height><uuid>45c2ed76-00c0-4fbe-86d4-9aab7ffe9e1e</uuid></wall><wall><x>2108.0</x><y>-479.0</y><rotation>0.9453113</rotation><width>199.83243</width><height>24.0</height><uuid>4031151c-317c-44ec-be32-5ddaa50cc2e4</uuid></wall><wall><x>2152.0</x><y>-333.0</y><rotation>1.8203833</rotation><width>157.89236</width><height>24.0</height><uuid>81d459a3-2602-4da9-8c7a-ddb5257c5410</uuid></wall><wall><x>1732.0</x><y>-949.0</y><rotation>-0.24335529</rotation><width>149.40215</width><height>24.0</height><uuid>201c0e2f-ef52-46dd-b400-d842dafe9de1</uuid></wall><wall><x>1897.0</x><y>-970.0</y><rotation>-0.16388756</rotation><width>203.96078</width><height>24.0</height><uuid>d120705e-6205-434b-b710-c649bac92c0e</uuid></wall><wall><x>2085.0</x><y>-901.0</y><rotation>0.5880026</rotation><width>219.93863</width><height>24.0</height><uuid>655ac11c-7b7f-49f1-8fc5-9c3505ab898a</uuid></wall><wall><x>2258.0</x><y>-762.0</y><rotation>0.7684506</rotation><width>208.62646</width><height>24.0</height><uuid>7fd78e15-4743-44b1-8ca4-9ae301142b4f</uuid></wall><wall><x>2386.0</x><y>-579.0</y><rotation>1.2629337</rotation><width>227.70595</width><height>24.0</height><uuid>8bca4968-8649-4cf3-a62f-2f95a6f2518b</uuid></wall><wall><x>2447.0</x><y>-371.0</y><rotation>1.2868185</rotation><width>217.72</width><height>24.0</height><uuid>505e47c6-6eae-49e5-9bd9-0f21d91abb47</uuid></wall><wall><x>2086.5625</x><y>-174.51068</y><rotation>2.0985382</rotation><width>216.44861</width><height>24.0</height><uuid>de321939-f619-4d14-8cc1-e4e318887ded</uuid></wall><wall><x>2003.75</x><y>18.574463</y><rotation>1.8261793</rotation><width>209.80467</width><height>24.0</height><uuid>1734a4d2-92e7-41c3-993f-71fc2beee506</uuid></wall><wall><x>2010.0</x><y>243.57452</y><rotation>1.3222558</rotation><width>272.36923</width><height>24.0</height><uuid>0bc8f28d-fbdd-497f-ac84-bafaf95d5811</uuid></wall><wall><x>2508.0</x><y>-196.0</y><rotation>1.1791887</rotation><width>217.46265</width><height>24.0</height><uuid>1c4f02d3-b099-450a-9ee4-d26ea44c0fac</uuid></wall><wall><x>2530.3125</x><y>5.8085403</y><rotation>1.7544186</rotation><width>213.59073</width><height>24.0</height><uuid>0c2dc4b3-a3b7-453e-bec6-50c04991a717</uuid></wall><wall><x>2470.9375</x><y>205.27664</y><rotation>1.9558316</rotation><width>205.00975</width><height>24.0</height><uuid>d01b8440-037f-4203-8892-91c6b86e7909</uuid></wall><wall><x>2056.0625</x><y>466.68094</y><rotation>1.4231604</rotation><width>197.14462</width><height>24.0</height><uuid>48d02570-cac8-4e83-920c-95935915d9d1</uuid></wall><wall><x>2206.0625</x><y>643.8085</y><rotation>0.56345206</rotation><width>327.6492</width><height>24.0</height><uuid>41bccb1e-c8d2-43a6-9ce3-f97a472555ca</uuid></wall><wall><x>2527.9375</x><y>741.14905</y><rotation>0.06290901</rotation><width>381.75516</width><height>24.0</height><uuid>05df7c03-0c63-43e4-8972-7964ece566db</uuid></wall><wall><x>2902.9375</x><y>698.06384</y><rotation>-0.29386935</rotation><width>397.02014</width><height>24.0</height><uuid>1bcda2f1-5458-48b3-a2cd-20e5abe96afc</uuid></wall><wall><x>3199.8125</x><y>495.40433</y><rotation>-0.91252834</rotation><width>379.24133</width><height>24.0</height><uuid>e75400fc-1a3b-4428-aa45-b1cd0a998f27</uuid></wall><wall><x>2512.3125</x><y>359.76596</y><rotation>0.733831</rotation><width>219.49487</width><height>24.0</height><uuid>aca6905e-d9de-4f1d-89ac-8a02ab6a0f00</uuid></wall><wall><x>2731.0625</x><y>423.59576</y><rotation>-0.047745258</rotation><width>293.3343</width><height>24.0</height><uuid>f9810c61-aa09-4318-a8f0-4e4281f74280</uuid></wall><wall><x>2915.4375</x><y>331.0425</y><rotation>-1.0381527</rotation><width>220.55385</width><height>24.0</height><uuid>a79dd9b1-6a87-4fb5-b593-cd53c3a07dd2</uuid></wall><wall><x>2987.3125</x><y>131.57442</y><rotation>-1.4163752</rotation><width>214.55302</width><height>24.0</height><uuid>c10b7fe9-d6db-4705-b696-8b0fbe9e606a</uuid></wall><wall><x>3320.125</x><y>155.51065</y><rotation>-1.5194277</rotation><width>389.5138</width><height>24.0</height><uuid>2d76585e-21d7-4fc0-8a09-938fe64fa2ed</uuid></wall><wall><x>3367.125</x><y>-129.76593</y><rotation>-1.2536682</rotation><width>205.23401</width><height>24.0</height><uuid>4faf89e3-5aa2-4583-b0ba-3d19812a3617</uuid></wall><wall><x>3467.125</x><y>-294.1276</y><rotation>-0.8122374</rotation><width>210.79373</width><height>24.0</height><uuid>77bd50a2-89da-474a-b663-510820ecc97a</uuid></wall><wall><x>3598.375</x><y>-386.68094</y><rotation>-0.23804636</rotation><width>139.94641</width><height>24.0</height><uuid>ab87be98-c36a-404f-b76b-a357f36ee5bf</uuid></wall><wall><x>3701.5</x><y>-399.44675</y><rotation>0.06512516</rotation><width>92.19544</width><height>24.0</height><uuid>3fecd4d4-f8eb-4466-948c-646abf28c0bb</uuid></wall><wall><x>3798.375</x><y>-370.72336</y><rotation>0.5172874</rotation><width>133.46161</width><height>24.0</height><uuid>a7963291-0d55-42dc-af00-071f726e61b3</uuid></wall><wall><x>3898.375</x><y>-286.149</y><rotation>0.90400976</rotation><width>137.43726</width><height>24.0</height><uuid>6dd448be-174d-49c2-8497-333ef053b11f</uuid></wall><wall><x>3956.1875</x><y>-190.40422</y><rotation>1.152572</rotation><width>88.63972</width><height>24.0</height><uuid>5b2ebe4c-32d3-40d6-af1e-24bb138d1a7e</uuid></wall><wall><x>3976.5</x><y>-110.617004</y><rotation>1.4735432</rotation><width>82.38932</width><height>24.0</height><uuid>9062ee3d-46c8-4a77-b7e6-89c645c546e3</uuid></wall><wall><x>4003.0625</x><y>-19.659576</y><rotation>1.1071488</rotation><width>118.511604</width><height>24.0</height><uuid>01349c50-314d-4caf-b1a3-69dd63d53832</uuid></wall><wall><x>4064.8125</x><y>59.297848</y><rotation>0.6828183</rotation><width>96.67471</width><height>24.0</height><uuid>7745c438-7f5b-4716-8f35-2283e69c060a</uuid></wall><wall><x>4167.9375</x><y>100.787224</y><rotation>0.18857226</rotation><width>133.36417</width><height>24.0</height><uuid>0585b9e5-9aa0-4145-9b83-fb6d7b44e286</uuid></wall><wall><x>4302.3125</x><y>96.00001</y><rotation>-0.15808997</rotation><width>139.74261</width><height>24.0</height><uuid>2fb63b9a-fe0c-4ef8-84b6-099cc3d492d1</uuid></wall><wall><x>3019.1875</x><y>-58.595734</y><rotation>-1.3924781</rotation><width>174.77129</width><height>24.0</height><uuid>abe28ab6-6bc2-44ea-bb5a-daaf89fc7901</uuid></wall><wall><x>3041.0625</x><y>-229.34045</y><rotation>-1.5013893</rotation><width>187.45132</width><height>24.0</height><uuid>8156fb36-26e9-4047-8e58-fdd5afdb1945</uuid></wall><wall><x>3092.625</x><y>-392.10635</y><rotation>-0.94372565</rotation><width>170.423</width><height>24.0</height><uuid>e9758388-8dc0-460b-86e8-0f1bb9f0497e</uuid></wall><wall><x>3400.4375</x><y>-717.6383</y><rotation>-0.5540923</rotation><width>188.15154</width><height>24.0</height><uuid>61f8e425-25eb-4abe-958c-a77d293a2b58</uuid></wall><wall><x>3553.5625</x><y>-781.46814</y><rotation>-0.24207169</rotation><width>166.86522</width><height>24.0</height><uuid>69f4dbcf-ca55-4a02-bd91-0a2e28d17a13</uuid></wall><wall><x>3218.0</x><y>-555.0</y><rotation>-0.8300743</rotation><width>300.82056</width><height>24.0</height><uuid>f2d7218e-f20e-491e-8107-16061154d900</uuid></wall><wall><x>3713.0</x><y>-786.0</y><rotation>0.20152777</rotation><width>189.84204</width><height>24.0</height><uuid>e3f0951f-3577-481b-8f32-6ce345ca01bb</uuid></wall><wall><x>3864.0</x><y>-721.0</y><rotation>0.63245183</rotation><width>181.01105</width><height>24.0</height><uuid>2fb7a72b-00fa-402a-857d-4fd6342cb296</uuid></wall><wall><x>3997.0</x><y>-631.0</y><rotation>0.5604569</rotation><width>171.18996</width><height>24.0</height><uuid>816afd79-b092-4f6e-abb8-82abf45c6925</uuid></wall><wall><x>4096.0</x><y>-536.0</y><rotation>0.8794594</rotation><width>150.57224</width><height>24.0</height><uuid>704e3ea4-a0fa-481c-8ead-b4743ed5323a</uuid></wall><wall><x>4175.0</x><y>-397.0</y><rotation>1.141034</rotation><width>184.8053</width><height>24.0</height><uuid>422f7093-b93b-4bb6-a340-4d6342629b5e</uuid></wall><wall><x>4231.0</x><y>-292.0</y><rotation>0.52278906</rotation><width>68.09552</width><height>24.0</height><uuid>db621f12-6e09-41b4-aa89-cdbec2748b4d</uuid></wall><wall><x>4287.0</x><y>-277.0</y><rotation>-0.07982998</rotation><width>75.23962</width><height>24.0</height><uuid>7ac5969c-48f4-4c92-8873-3b3d29df12fa</uuid></wall><wall><x>4342.0</x><y>-300.0</y><rotation>-0.62548506</rotation><width>88.814415</width><height>24.0</height><uuid>0e163cfd-6b35-439f-a5e5-ca2bb67a373c</uuid></wall><wall><x>4373.875</x><y>-368.1702</y><rotation>-1.5707964</rotation><width>105.0</width><height>24.0</height><uuid>bbe561b4-92c5-4c86-8950-baba7c526617</uuid></wall><wall><x>4432.0</x><y>84.0</y><rotation>-0.23052184</rotation><width>100.6628</width><height>24.0</height><uuid>99f00919-fa53-4c4e-8e37-b0d1112e25d1</uuid></wall><wall><x>4520.0</x><y>39.0</y><rotation>-0.6693574</rotation><width>116.03879</width><height>24.0</height><uuid>4cd6b225-fb01-483f-b7ab-4fa33a6e45f7</uuid></wall><wall><x>4595.0</x><y>-39.0</y><rotation>-0.9798783</rotation><width>95.131485</width><height>24.0</height><uuid>a7d98629-1dde-43b3-8b18-41b57a6cef22</uuid></wall><wall><x>4635.0</x><y>-127.0</y><rotation>-1.2739516</rotation><width>88.88757</width><height>24.0</height><uuid>d54388bc-5c1d-4219-9df8-612d710cf984</uuid></wall><wall><x>4659.8125</x><y>-205.40422</y><rotation>-1.19029</rotation><width>80.77747</width><height>24.0</height><uuid>e7daade4-e5a0-47c4-a152-b17069adeba6</uuid></wall><wall><x>4680.125</x><y>-310.72336</y><rotation>-1.4651307</rotation><width>132.74034</width><height>24.0</height><uuid>0953a71a-350b-4692-82fd-2bd67906994a</uuid></wall><wall><x>4739.5</x><y>-443.1702</y><rotation>-0.9398948</rotation><width>174.61386</width><height>24.0</height><uuid>9e075518-86a0-4c56-a3df-c82f57587d63</uuid></wall><wall><x>4487.9375</x><y>-679.34045</y><rotation>-1.1541729</rotation><width>570.82837</width><height>24.0</height><uuid>93d9f572-2270-4a84-93b0-29b497419540</uuid></wall><wall><x>5099.0</x><y>-236.0</y><rotation>0.6976927</rotation><width>823.40875</width><height>24.0</height><uuid>becdaadc-2a37-4d6f-9966-2810fe3572c1</uuid></wall><wall><x>5101.0</x><y>-632.0</y><rotation>0.5756491</rotation><width>1212.3898</width><height>24.0</height><uuid>3e1fb17a-7f04-4e34-99ab-52a729b5ab3c</uuid></wall><wall><x>5498.0</x><y>-134.0</y><rotation>-1.0094445</rotation><width>385.0987</width><height>24.0</height><uuid>b7274fec-8b0b-4329-80c4-7fb2736046d5</uuid></wall><powerup><x>-259.0</x><y>-234.0</y><uuid>1322d689-341e-493c-864a-cde2f3cef820</uuid></powerup><powerup><x>-454.0</x><y>78.0</y><uuid>38452a13-d04a-4d95-a571-aa738d24546d</uuid></powerup><powerup><x>-207.0</x><y>327.0</y><uuid>85caecde-fd85-4fc9-9bc3-00681256307a</uuid></powerup><powerup><x>-221.0</x><y>703.0</y><uuid>33773541-3c03-4d6b-980c-67f55dbb939f</uuid></powerup><powerup><x>178.0</x><y>708.0</y><uuid>183ac2ae-f616-4269-befc-52dc9f9b02c5</uuid></powerup><powerup><x>621.0</x><y>688.0</y><uuid>33bc2381-90ba-48ba-b466-98ea582af22a</uuid></powerup><powerup><x>810.0</x><y>510.0</y><uuid>02deff05-f98b-4daa-a8c2-2403c3b49f36</uuid></powerup><powerup><x>810.0</x><y>309.0</y><uuid>3a30224b-70dc-4400-9f93-9316d768ba26</uuid></powerup><powerup><x>818.0</x><y>59.0</y><uuid>526631ae-5b23-428d-b1e6-4de3ce784be7</uuid></powerup><powerup><x>1477.0</x><y>-607.0</y><uuid>1c716981-c141-4ac7-8bba-08fb41edd8ca</uuid></powerup><powerup><x>1655.0</x><y>-774.0</y><uuid>f3aedeb3-c556-46c6-9513-3d4374287389</uuid></powerup><powerup><x>1896.0</x><y>-803.0</y><uuid>16efec30-f45e-4c9c-9e84-89ad23ea8cff</uuid></powerup><powerup><x>2135.0</x><y>-683.0</y><uuid>bd5774d0-9902-4a31-a17d-d7fed651dce6</uuid></powerup><powerup><x>2426.0</x><y>-55.0</y><uuid>b8b9d0b3-6f09-46b7-8b79-a2581cfb4095</uuid></powerup><powerup><x>2059.0</x><y>140.0</y><uuid>dba6f6fc-6f72-4c8d-8289-f782bfa2a963</uuid></powerup><powerup><x>2658.0</x><y>599.0</y><uuid>e6f4aaa8-cccf-4805-9fbe-9b0f91b1732f</uuid></powerup><powerup><x>2942.0</x><y>531.0</y><uuid>1a2c8a26-c7cc-4d8d-87b2-3fb03bc0705f</uuid></powerup><powerup><x>3142.0</x><y>254.0</y><uuid>8fe7403a-ab9f-4c07-bf27-da564d5e451f</uuid></powerup><powerup><x>3386.0</x><y>-496.0</y><uuid>3e1751c4-34e9-41ef-83b6-f80a671b9895</uuid></powerup><powerup><x>3698.0</x><y>-599.0</y><uuid>2f8f4372-d1d0-4d16-8efe-190026516c04</uuid></powerup><powerup><x>3972.0</x><y>-423.0</y><uuid>51337243-8e14-4429-97b9-2e660fa9ac83</uuid></powerup><powerup><x>4170.0</x><y>-98.0</y><uuid>01f37e01-fa15-4194-9e20-969d5e16f178</uuid></powerup><powerup><x>4542.0</x><y>-297.0</y><uuid>2e7694e7-75a6-4a34-81b0-4706a26cd3bd</uuid></powerup><powerup><x>4704.0</x><y>-721.0</y><uuid>a32c8cc0-e109-478b-ab99-b7a1114dbef7</uuid></powerup></root><root><missleOrigin><x>-1782.0895</x><y>-681.37476</y><angle>-0.5026549</angle></missleOrigin><earthOrigin><x>250.62317</x><y>96.609886</y></earthOrigin><isCameraFollowsMissle>true</isCameraFollowsMissle><fileName>Rana-569</fileName><creatorId>057dbe61</creatorId><uuid>939ae3c4-8163-43c8-9dad-3e796c4fad12</uuid><isbuiltin>false</isbuiltin><wall><x>7.243088</x><y>-90.42518</y><rotation>0.12184244</rotation><width>345.56186</width><height>24.0</height><uuid>01c7acff-feea-469a-b3c5-4f710bd19f1b</uuid></wall><wall><x>-20.690002</x><y>-19.850544</y><rotation>2.9300601</rotation><width>407.41257</width><height>24.0</height><uuid>cd304e4e-a9d7-41d2-923e-8466086ef6fc</uuid></wall><wall><x>-936.7562</x><y>-42.17206</y><rotation>-3.055775</rotation><width>1458.367</width><height>24.0</height><uuid>e0845c7c-bc28-4994-80f6-b769150bef9e</uuid></wall><wall><x>-1583.0</x><y>-975.0</y><rotation>-0.53468174</rotation><width>1165.6951</width><height>24.0</height><uuid>8255bebc-0693-4c72-900e-52091722e760</uuid></wall><wall><x>-1285.3629</x><y>-1392.7754</y><rotation>-2.468168</rotation><width>474.6125</width><height>24.0</height><uuid>9f292012-4a59-4369-bc8a-7fd710dc04cc</uuid></wall><wall><x>-107.22182</x><y>-1592.2913</y><rotation>0.39555565</rotation><width>1308.0004</width><height>24.0</height><uuid>3f563b8c-db78-46d0-bd89-c8cbca43c270</uuid></wall><wall><x>1116.341</x><y>-1208.9902</y><rotation>0.21054867</rotation><width>1282.3182</width><height>24.0</height><uuid>fd7ed368-84e7-4338-84aa-cb52f31cc7bb</uuid></wall><wall><x>666.0</x><y>323.0</y><rotation>2.852538</rotation><width>1634.8226</width><height>24.0</height><uuid>98581410-eaf3-4a4e-88f4-ffc909663c6c</uuid></wall><wall><x>-866.06055</x><y>545.8128</y><rotation>-3.1337187</rotation><width>1524.0472</width><height>24.0</height><uuid>5c0b78dd-fce9-4a3d-b7a9-767c605524cc</uuid></wall><wall><x>141.17087</x><y>-159.38179</y><rotation>-0.14350589</rotation><width>615.32513</width><height>24.0</height><uuid>38b35749-b46f-4fbd-8a8b-34e9d6bd33e8</uuid></wall><wall><x>425.5442</x><y>9.536684</y><rotation>1.7169983</rotation><width>459.90652</width><height>24.0</height><uuid>debd4e05-8b4d-4bab-baff-8966044e7272</uuid></wall><wall><x>144.559</x><y>250.22137</y><rotation>3.0441427</rotation><width>493.34067</width><height>24.0</height><uuid>4d8c7a77-c478-4bcd-8366-3dfcfb4ed571</uuid></wall><wall><x>-530.0</x><y>279.0</y><rotation>3.1322472</rotation><width>856.03735</width><height>24.0</height><uuid>de0a33a4-e066-4881-b309-79801ede49be</uuid></wall><wall><x>-1317.7601</x><y>263.74927</y><rotation>3.1975303</rotation><width>742.7961</width><height>24.0</height><uuid>6c1f1cd1-59ec-43c8-8cbb-6e67a563ca5c</uuid></wall><wall><x>-2323.0</x><y>288.0</y><rotation>-2.4119506</rotation><width>913.58746</width><height>24.0</height><uuid>a68c7095-4e32-4de5-9d29-c17307252cd4</uuid></wall><wall><x>-1801.0</x><y>565.0</y><rotation>-0.17005862</rotation><width>401.79596</width><height>24.0</height><uuid>d96632de-c5b3-4805-b87b-2304ed03f80f</uuid></wall><wall><x>-1794.0</x><y>-418.0</y><rotation>-2.0170653</rotation><width>720.5699</width><height>24.0</height><uuid>1ca23ed6-c94c-4477-b2ff-ee9ddae3f1fe</uuid></wall><wall><x>-2401.0</x><y>44.0</y><rotation>0.20701064</rotation><width>530.3226</width><height>24.0</height><uuid>dbf43990-8039-442d-b43b-61bbd03ce376</uuid></wall><wall><x>-2070.5227</x><y>49.464996</y><rotation>-0.52654165</rotation><width>234.80417</width><height>24.0</height><uuid>10fc8162-a06c-4b06-a536-deb6000696e5</uuid></wall><wall><x>-2033.0</x><y>-346.0</y><rotation>1.4240819</rotation><width>704.5694</width><height>24.0</height><uuid>60e92f3e-c32a-410e-b808-fdbd4488334e</uuid></wall><wall><x>1366.1611</x><y>-100.77579</y><rotation>-1.9346676</rotation><width>413.04358</width><height>24.0</height><uuid>417b27cc-d2f8-451a-ad98-3e688294675a</uuid></wall><wall><x>1126.6097</x><y>-485.9171</y><rotation>-2.2912452</rotation><width>522.9474</width><height>24.0</height><uuid>63ad98d7-4900-4055-8256-7f3aefe71012</uuid></wall><wall><x>1699.0</x><y>-810.0</y><rotation>1.7715766</rotation><width>521.47577</width><height>24.0</height><uuid>fb1c808f-2878-4f59-a2d4-a202595ce5e7</uuid></wall><wall><x>1289.0525</x><y>-630.26636</y><rotation>0.18150619</rotation><width>703.5574</width><height>24.0</height><uuid>33cf4640-c6a7-4098-aade-42c199d08dab</uuid></wall><wall><x>-1384.76</x><y>-641.5853</y><rotation>-0.36274976</rotation><width>941.2524</width><height>24.0</height><uuid>5cfc298d-c220-4e29-8c43-f0616db78394</uuid></wall><wall><x>-842.80286</x><y>-898.7605</y><rotation>-0.6998929</rotation><width>298.07382</width><height>24.0</height><uuid>abb7169d-f40e-43b8-b60a-6599d63c274d</uuid></wall><wall><x>-688.0</x><y>-1088.0</y><rotation>-1.1623074</rotation><width>219.02055</width><height>24.0</height><uuid>1bf0d79a-60b6-4d46-bf0c-53ca7bd6ce7c</uuid></wall><wall><x>-656.6963</x><y>-1283.5796</y><rotation>-1.6619139</rotation><width>197.82063</width><height>24.0</height><uuid>75343583-9c42-461c-b882-8e4f5a03c2d4</uuid></wall><wall><x>-1075.0</x><y>-1695.0</y><rotation>-0.3822489</rotation><width>852.52856</width><height>24.0</height><uuid>162ba389-903a-4ef0-addb-e76eed1e2dec</uuid></wall><wall><x>-604.0176</x><y>-1409.7408</y><rotation>-0.23305205</rotation><width>157.15598</width><height>24.0</height><uuid>37f9fc55-a435-47f7-ac9c-df2231aa43c3</uuid></wall><wall><x>-145.98135</x><y>-1283.5796</y><rotation>0.347753</rotation><width>839.23596</width><height>24.0</height><uuid>b9ef1b05-e03e-41f0-948b-436d811fc164</uuid></wall><wall><x>606.5185</x><y>-921.28406</y><rotation>0.55092335</rotation><width>856.81854</width><height>24.0</height><uuid>8add5a13-204e-4543-a817-ecc9441772c5</uuid></wall><wall><x>597.0</x><y>-723.0</y><rotation>-3.0017414</rotation><width>760.4242</width><height>24.0</height><uuid>d730bc18-6058-4e2d-8f7e-909d1ab0c646</uuid></wall><wall><x>25.26773</x><y>-881.56085</y><rotation>-2.7053623</rotation><width>456.77676</width><height>24.0</height><uuid>145e1f68-0661-4abe-9905-fa64c2f547d8</uuid></wall><wall><x>-232.76819</x><y>-977.5715</y><rotation>-3.0697167</rotation><width>125.32358</width><height>24.0</height><uuid>de6e067a-53a3-415d-9a6d-6ad0437d273f</uuid></wall><wall><x>-342.0</x><y>-954.0</y><rotation>2.7030294</rotation><width>124.81186</width><height>24.0</height><uuid>9ee25377-f2f7-44d5-afc3-cdecfdc06e92</uuid></wall><wall><x>-469.0</x><y>-820.0</y><rotation>2.1273956</rotation><width>265.0</width><height>24.0</height><uuid>c6c57af6-e8cd-4533-99ac-4fe32c6accf7</uuid></wall><wall><x>-686.0</x><y>-612.0</y><rotation>2.62523</rotation><width>356.4772</width><height>24.0</height><uuid>8fe21cef-a5e1-4922-a38f-2cde3d9f2c68</uuid></wall><wall><x>-1140.2314</x><y>-389.68774</y><rotation>2.905289</rotation><width>615.0935</width><height>24.0</height><uuid>b868efbb-5621-496b-8903-2229dcf543ff</uuid></wall><wall><x>-1584.1405</x><y>-313.02048</y><rotation>3.1240456</rotation><width>304.3846</width><height>24.0</height><uuid>55f2bfb2-b2a9-400a-9c6c-28afd853173c</uuid></wall><wall><x>-833.0</x><y>-498.0</y><rotation>1.7894653</rotation><width>73.756355</width><height>24.0</height><uuid>0461c9f4-d22d-4cb6-9315-fe78f6ff74b2</uuid></wall><wall><x>-881.0</x><y>-104.5</y><rotation>5.9606695</rotation><width>437.3098</width><height>24.0</height><uuid>23e2c770-03a0-4ff2-8b4b-fb3a47b84876</uuid></wall><wall><x>-609.0</x><y>-223.0</y><rotation>5.6742277</rotation><width>209.31055</width><height>24.0</height><uuid>97109992-77ad-439f-bdd4-cca7dc459cc4</uuid></wall><wall><x>-478.0</x><y>-353.5</y><rotation>5.3295803</rotation><width>214.06578</width><height>24.0</height><uuid>39b26d74-83fc-41da-9a70-ffbfc5f410a1</uuid></wall><wall><x>-342.0</x><y>-519.0</y><rotation>5.456391</rotation><width>263.20703</width><height>24.0</height><uuid>7fedd06f-f247-4da8-a717-944d602a9985</uuid></wall><wall><x>-138.0</x><y>-608.0</y><rotation>6.275051</rotation><width>270.00812</width><height>24.0</height><uuid>ff27f44f-511c-4d57-bf82-94c2e37504cb</uuid></wall><wall><x>-17.0</x><y>-526.0</y><rotation>1.5948881</rotation><width>190.04819</width><height>24.0</height><uuid>9d9a5f6f-9fa4-41df-a56e-8577e5bd026f</uuid></wall><wall><x>-133.5</x><y>-437.0</y><rotation>3.0892391</rotation><width>253.3142</width><height>24.0</height><uuid>3d3de3b0-5b42-4c31-924c-78b50faa39ab</uuid></wall><wall><x>-300.0</x><y>-292.0</y><rotation>1.9287784</rotation><width>320.81644</width><height>24.0</height><uuid>1d42bf3b-2ee5-493c-ad08-8e66483677be</uuid></wall><wall><x>-264.0</x><y>-131.5</y><rotation>0.23962408</rotation><width>205.17671</width><height>24.0</height><uuid>2be4fc8e-6f49-400e-ac4f-c015f215b0c9</uuid></wall><wall><x>416.0</x><y>-484.0</y><rotation>4.526373</rotation><width>575.5143</width><height>24.0</height><uuid>88dd700e-a81d-4106-ade6-1edd46f655ff</uuid></wall><wall><x>716.5</x><y>-449.5</y><rotation>2.382937</rotation><width>711.55365</width><height>24.0</height><uuid>ca45e1a9-c370-4b01-ba99-d0322824f086</uuid></wall><wall><x>467.0</x><y>-213.0</y><rotation>1.5707964</rotation><width>24.0</width><height>24.0</height><uuid>81bdd648-7ba8-428d-bb39-618dab31fc42</uuid></wall><powerup><x>-1425.0</x><y>-844.0</y><uuid>c59e60fc-5ae0-44e2-9ebf-862a16284195</uuid></powerup><powerup><x>-1060.0</x><y>-1062.0</y><uuid>8c95b978-9613-412d-b98e-ec4adc055dad</uuid></powerup><powerup><x>-871.0</x><y>-1318.0</y><uuid>a555d573-c887-4670-956e-570cd1472266</uuid></powerup><powerup><x>-634.0</x><y>-1600.0</y><uuid>aa70ef7b-a1f1-4567-b190-8868eae2a2b0</uuid></powerup><powerup><x>-275.0</x><y>-1476.0</y><uuid>ef44ce3b-ffbf-4ae9-897e-c41f08a0bd12</uuid></powerup><powerup><x>-93.0</x><y>-1403.0</y><uuid>926a114d-f32f-4410-917d-203bb8b66735</uuid></powerup><powerup><x>498.0</x><y>-1183.0</y><uuid>68a4898f-8901-4513-91ae-d170cb6265c3</uuid></powerup><powerup><x>888.0</x><y>-1051.0</y><uuid>e3a0738c-524c-4165-951e-56e56ff21ba6</uuid></powerup><powerup><x>1228.0</x><y>-923.0</y><uuid>c30ce712-397a-432b-a47a-acea8a8acbc4</uuid></powerup><powerup><x>-1208.0</x><y>-542.0</y><uuid>93191f31-1372-41a0-9bc1-d4764871cc53</uuid></powerup><powerup><x>-795.0</x><y>-727.0</y><uuid>a51d25ff-9196-412d-bb57-b2b7aa7e203f</uuid></powerup><powerup><x>-572.0</x><y>-929.0</y><uuid>d5affe16-4e51-4c37-85cf-6bad86a00c6d</uuid></powerup><powerup><x>-354.0</x><y>-1232.0</y><uuid>45b5a7b3-f903-4fb1-8c5c-0185ff8cf61d</uuid></powerup><powerup><x>0.0</x><y>-1093.0</y><uuid>8e62491a-22e1-41fa-9811-83f577d17c89</uuid></powerup><powerup><x>252.0</x><y>-954.0</y><uuid>7c25cca9-3303-4736-97dc-a3e644210c24</uuid></powerup><powerup><x>-762.0</x><y>-314.0</y><uuid>4b8c0fc6-db68-42b4-a862-dd4fccd3dc6e</uuid></powerup><powerup><x>-460.0</x><y>-568.0</y><uuid>4e290502-e9a5-43f0-b1d5-bf96a0b0ecd5</uuid></powerup><powerup><x>-242.0</x><y>-825.0</y><uuid>8db57fe9-8ed1-45d7-8008-5764302ba2c2</uuid></powerup><powerup><x>131.0</x><y>-679.0</y><uuid>41b34227-5464-40d0-b75c-bb7260d91489</uuid></powerup><powerup><x>148.0</x><y>-397.0</y><uuid>372df48f-ed66-4483-94a3-c571762618a4</uuid></powerup><powerup><x>851.0</x><y>33.0</y><uuid>0045e4a2-91d4-430e-9bbc-f56fca76b228</uuid></powerup><powerup><x>437.0</x><y>317.0</y><uuid>158ee542-e6b9-4bd3-b9d1-eb28bb5a2c3e</uuid></powerup><powerup><x>5.0</x><y>395.0</y><uuid>502c2c26-da77-4cf2-84db-810f18c5b591</uuid></powerup><powerup><x>-269.0</x><y>420.0</y><uuid>9f3b61b3-fcfa-4ec5-97b6-4249a5b40f60</uuid></powerup><powerup><x>-731.0</x><y>405.0</y><uuid>887b69a9-1da9-4318-a5c0-6dec7b970481</uuid></powerup><powerup><x>-1327.0</x><y>410.0</y><uuid>12a5951e-842f-4de2-9a5e-f92481d6d74b</uuid></powerup><powerup><x>-1810.0</x><y>395.0</y><uuid>a0a11d8b-446d-4e41-aca5-39164c846b7e</uuid></powerup><powerup><x>-1911.0</x><y>163.0</y><uuid>1a8d6814-7ef3-457e-8bd6-abefa488a087</uuid></powerup><powerup><x>-1658.0</x><y>38.0</y><uuid>ed2c9df8-4081-46c6-b3b5-612cab962723</uuid></powerup><powerup><x>-956.0</x><y>102.0</y><uuid>e63f9c06-c7f9-4c25-8bcd-5e2c840e04f2</uuid></powerup><powerup><x>-634.0</x><y>116.0</y><uuid>7162389a-f3b1-4568-87aa-6f982595e033</uuid></powerup><powerup><x>-197.0</x><y>139.0</y><uuid>1111c920-ee43-4c73-ba3e-35b02b0ecb83</uuid></powerup><warp><x>1393.0</x><y>-863.0</y><rotation>0.0</rotation><name>WARP-5efd31</name><connectsTo>WARP-e4332f</connectsTo><uuid>05efd310-c9aa-4c25-b5b7-984c5adde3b9</uuid></warp><warp><x>-1689.0</x><y>-410.0</y><rotation>0.0</rotation><name>WARP-e4332f</name><connectsTo>WARP-e4332f</connectsTo><uuid>3e4332f1-6a9e-49e5-9e73-011f91a96bfd</uuid></warp><warp><x>456.0</x><y>-864.0</y><rotation>0.0</rotation><name>WARP-947361</name><connectsTo>WARP-5fa70f</connectsTo><uuid>29473613-0420-4000-a496-b65f5d61a373</uuid></warp><warp><x>-1315.2316</x><y>-274.9332</y><rotation>0.0</rotation><name>WARP-5fa70f</name><connectsTo>WARP-5fa70f</connectsTo><uuid>a5fa70fe-6c8c-4f34-b279-3482d2a25866</uuid></warp><warp><x>-133.0</x><y>-219.0</y><rotation>0.0</rotation><name>WARP-408592</name><connectsTo>WARP-2ac85d</connectsTo><uuid>04085924-5799-436d-9cbe-26de4bf182f0</uuid></warp><warp><x>1089.0</x><y>-228.0</y><rotation>0.0</rotation><name>WARP-2ac85d</name><connectsTo>WARP-2ac85d</connectsTo><uuid>f2ac85d5-d972-4114-941a-3fe3d821fed9</uuid></warp></root><root><missleOrigin><x>0.0</x><y>0.0</y><angle>0.0</angle></missleOrigin><earthOrigin><x>1336.0</x><y>12.0</y></earthOrigin><isCameraFollowsMissle>true</isCameraFollowsMissle><fileName>Sharatan-125</fileName><creatorId>057dbe61</creatorId><uuid>c6980dca-d3f1-4cd2-a1b3-2222fe08ec3e</uuid><isbuiltin>false</isbuiltin><wall><x>-179.0</x><y>-42.0</y><rotation>-1.0253947</rotation><width>240.9585</width><height>16.0</height><uuid>4c7a2e3e-0ea3-46b3-86af-e08f174b37cc</uuid></wall><wall><x>-91.0</x><y>138.0</y><rotation>0.4901407</rotation><width>320.7647</width><height>16.0</height><uuid>7f7d296d-66f1-4ed5-834b-897387a5da3f</uuid></wall><wall><x>117.50589</x><y>-101.09084</y><rotation>0.14973295</rotation><width>469.25046</width><height>16.0</height><uuid>fb4055d3-0d70-4582-8e77-07166890ccf9</uuid></wall><wall><x>374.96414</x><y>313.25323</y><rotation>0.298932</rotation><width>679.1178</width><height>16.0</height><uuid>80a14d6c-b4fd-4be7-8d54-c7fb14ae4f23</uuid></wall><wall><x>704.4288</x><y>87.57425</y><rotation>0.4084361</rotation><width>777.9955</width><height>16.0</height><uuid>cde31625-08f3-46e8-a362-7efe8c754774</uuid></wall><wall><x>983.0713</x><y>489.55188</y><rotation>0.2652071</rotation><width>587.5415</width><height>16.0</height><uuid>8c790346-6104-4dc6-9c44-5c05fb45ece7</uuid></wall><wall><x>1253.1183</x><y>292.01862</y><rotation>0.2583436</rotation><width>399.2493</width><height>16.0</height><uuid>5ffb97d7-9eac-44e8-ae68-b38d101e580b</uuid></wall><wall><x>1368.0714</x><y>585.92395</y><rotation>0.18028696</rotation><width>206.34438</width><height>16.0</height><uuid>77d2a179-7d3f-445d-9fb4-fd3d9809d339</uuid></wall><wall><x>1587.7144</x><y>596.03174</y><rotation>-0.068996</rotation><width>246.5867</width><height>16.0</height><uuid>37f431f6-58ad-447f-8b4d-674ac471dd4f</uuid></wall><wall><x>1793.7859</x><y>554.1056</y><rotation>-0.38650632</rotation><width>185.69868</width><height>16.0</height><uuid>544e06b5-8243-436e-997d-67ca9bc56bd2</uuid></wall><wall><x>1561.7576</x><y>333.81223</y><rotation>-0.088749304</rotation><width>236.93248</width><height>16.0</height><uuid>6a2cbdb2-0d0b-441b-80be-8e7e3b06646d</uuid></wall><wall><x>1744.5714</x><y>290.93164</y><rotation>-0.42877802</rotation><width>153.93506</width><height>16.0</height><uuid>a45a5e2e-65a8-488d-b0e0-b90abb27503c</uuid></wall><wall><x>2018.3348</x><y>415.20468</y><rotation>-0.6446512</rotation><width>347.80023</width><height>16.0</height><uuid>d374fd54-1231-4054-8063-01e0e25b6415</uuid></wall><wall><x>1862.1066</x><y>202.85983</y><rotation>-0.8365164</rotation><width>152.2268</width><height>16.0</height><uuid>82e1d205-f991-4a2e-828b-7d4622184701</uuid></wall><wall><x>2172.274</x><y>261.8661</y><rotation>-1.237552</rotation><width>110.054535</width><height>16.0</height><uuid>a9cfc780-a113-4117-a3a7-3b3d67e7335d</uuid></wall><wall><x>1936.0</x><y>70.0</y><rotation>-1.2334569</rotation><width>142.00352</width><height>16.0</height><uuid>8a46c508-473e-4173-9c5a-9e6babe66f9f</uuid></wall><wall><x>2205.0</x><y>111.0</y><rotation>-1.4078698</rotation><width>147.95946</width><height>16.0</height><uuid>b394ac10-1873-485b-a40d-58801d6b387a</uuid></wall><wall><x>1962.6918</x><y>-56.251465</y><rotation>-1.4946082</rotation><width>131.38112</width><height>16.0</height><uuid>484dbddd-c114-478d-adf3-c62452abb374</uuid></wall><wall><x>2215.0134</x><y>-15.102386</y><rotation>-1.6068168</rotation><width>111.07205</width><height>16.0</height><uuid>8d4df524-4465-42ce-a4f6-9dc7c3af57de</uuid></wall><wall><x>1944.2162</x><y>-182.8043</y><rotation>-1.9177132</rotation><width>138.2353</width><height>16.0</height><uuid>83121802-7f35-476b-bb94-e50dcfd9d96e</uuid></wall><wall><x>2185.7275</x><y>-174.45804</y><rotation>-1.8257858</rotation><width>218.05045</width><height>16.0</height><uuid>f0265282-ce56-4b6e-93ed-d9c4f4e4a097</uuid></wall><wall><x>1875.3752</x><y>-291.1312</y><rotation>-2.3670635</rotation><width>130.11533</width><height>16.0</height><uuid>2f1c4bf4-2a4c-45e3-870f-e66afdc81cb0</uuid></wall><wall><x>2080.7278</x><y>-370.3238</y><rotation>-2.2634382</rotation><width>244.2949</width><height>16.0</height><uuid>8cd73990-2695-443b-bac4-abc3b1bf8e74</uuid></wall><wall><x>1767.1564</x><y>-347.80823</y><rotation>-2.972675</rotation><width>130.86252</width><height>16.0</height><uuid>d17012e0-5f02-40fe-88fb-06ecf585ed9c</uuid></wall><wall><x>1883.2278</x><y>-507.74622</y><rotation>-2.7921717</rotation><width>262.8859</width><height>16.0</height><uuid>aa138b76-e29b-43d3-a247-aa6cec26ca7e</uuid></wall><wall><x>1632.4421</x><y>-329.52396</y><rotation>-0.39234015</rotation><width>156.92355</width><height>16.0</height><uuid>577d74dd-9153-479e-80c1-9b3acac9b2d8</uuid></wall><wall><x>1688.1565</x><y>-545.7508</y><rotation>-0.09368468</rotation><width>149.65627</width><height>16.0</height><uuid>0d34187a-27c8-4ab7-8ec5-e79607178a77</uuid></wall><wall><x>1553.8706</x><y>-505.7796</y><rotation>-0.5043774</rotation><width>142.77956</width><height>16.0</height><uuid>f136d340-7416-4ab8-9e8d-083dc476584b</uuid></wall><wall><x>1083.0</x><y>161.0</y><rotation>-1.4536875</rotation><width>68.46897</width><height>16.0</height><uuid>9dbffe1c-641a-41ac-b8dc-a171e8ab6b30</uuid></wall><wall><x>1113.0</x><y>198.0</y><rotation>0.03844259</rotation><width>52.03845</width><height>16.0</height><uuid>39566757-8ea8-455b-99c1-2bd7613c6296</uuid></wall><wall><x>1151.0</x><y>185.0</y><rotation>-1.5253731</rotation><width>44.04543</width><height>16.0</height><uuid>2401cd6d-5e8c-494a-9606-e3179a2262d8</uuid></wall><wall><x>1121.0</x><y>158.0</y><rotation>0.019997334</rotation><width>50.01</width><height>16.0</height><uuid>2ce57ba7-bc1d-487b-a286-df1af08ee173</uuid></wall><wall><x>1165.0552</x><y>137.88309</y><rotation>-1.1117656</rotation><width>45.17743</width><height>16.0</height><uuid>a0879538-1b4c-4753-968f-eae531fe3b5d</uuid></wall><wall><x>1223.8412</x><y>182.7781</y><rotation>-1.497399</rotation><width>68.18358</width><height>16.0</height><uuid>7db83d0e-1f72-4cab-80ae-b62d9fb8fae3</uuid></wall><wall><x>1336.7996</x><y>219.79295</y><rotation>1.5707964</rotation><width>26.0</width><height>16.0</height><uuid>7b858c83-b07b-4d33-81d5-fea9e28f5e72</uuid></wall><wall><x>1308.2698</x><y>237.06776</y><rotation>0.0</rotation><width>30.0</width><height>16.0</height><uuid>871ffa93-9837-4a31-8a46-6619a533618f</uuid></wall><wall><x>1284.0</x><y>194.0</y><rotation>-1.4928297</rotation><width>64.195015</width><height>16.0</height><uuid>b134f536-aeff-4b5c-8b0c-710e58ea27b4</uuid></wall><wall><x>1311.6985</x><y>152.69273</y><rotation>0.0</rotation><width>60.0</width><height>16.0</height><uuid>e472f2e7-f2c6-4a52-9985-66f3613f2d62</uuid></wall><wall><x>1354.0</x><y>163.0</y><rotation>-1.1760052</rotation><width>13.0</width><height>16.0</height><uuid>74ebb86b-df8f-4c48-a31e-a1f352bfbbcd</uuid></wall><wall><x>1338.3345</x><y>180.83711</y><rotation>-0.02531105</rotation><width>79.025314</width><height>16.0</height><uuid>dcc17090-e323-4468-8a02-f4700739008b</uuid></wall><wall><x>1504.4282</x><y>269.1698</y><rotation>0.035699114</rotation><width>112.0714</width><height>16.0</height><uuid>6456429a-73dd-4304-8218-fbe18f13ab3b</uuid></wall><wall><x>1511.5464</x><y>211.52951</y><rotation>-1.5220544</rotation><width>82.0975</width><height>16.0</height><uuid>41b99ca8-2cb6-4c24-843c-606f5948b4ad</uuid></wall><wall><x>1538.7699</x><y>-260.42856</y><rotation>-0.9886588</rotation><width>94.57801</width><height>16.0</height><uuid>5b8b10c1-a9ba-4764-88a2-9719b039c4fd</uuid></wall><wall><x>1424.5063</x><y>-382.3228</y><rotation>-0.92467743</rotation><width>229.20079</width><height>16.0</height><uuid>edf02f52-a521-4ecf-94e8-d4c10e587ee0</uuid></wall><wall><x>1344.2698</x><y>-257.55554</y><rotation>1.8438051</rotation><width>77.88453</width><height>16.0</height><uuid>2cbcbabc-bc30-47c9-a8e0-a1dccee8365f</uuid></wall><wall><x>1506.413</x><y>-185.3507</y><rotation>1.8233505</rotation><width>64.03124</width><height>16.0</height><uuid>c1b9d47b-356d-4662-a8f3-1f69a5c953ea</uuid></wall><wall><x>1281.0555</x><y>-168.8523</y><rotation>2.379446</rotation><width>152.06906</width><height>16.0</height><uuid>a808c0d9-6c72-4113-a298-9424d13f26d2</uuid></wall><wall><x>1508.5557</x><y>-84.41916</y><rotation>1.4413866</rotation><width>147.23111</width><height>16.0</height><uuid>54d10f7d-46aa-4a65-bc73-0e2fbffe2099</uuid></wall><wall><x>1322.1125</x><y>85.15576</y><rotation>0.12511623</rotation><width>488.82104</width><height>16.0</height><uuid>ea583d7d-4a6c-4143-887c-ec6c2c617f70</uuid></wall><wall><x>1533.9126</x><y>47.37445</y><rotation>1.3336761</rotation><width>123.454445</width><height>16.0</height><uuid>0951025d-83cc-457b-945c-e3bbc872aa58</uuid></wall><wall><x>1161.4124</x><y>-32.400604</y><rotation>2.2215307</rotation><width>221.20578</width><height>16.0</height><uuid>24070cd4-a334-4176-a3d6-00ad5b8bab97</uuid></wall><wall><x>1406.0</x><y>200.0</y><rotation>-1.3934345</rotation><width>107.68937</width><height>16.0</height><uuid>6170e1aa-b8a1-4c64-9680-7742472b2f1f</uuid></wall><wall><x>1463.0</x><y>198.0</y><rotation>-1.2655731</rotation><width>76.537575</width><height>16.0</height><uuid>17df4653-9dc0-4d29-8f76-e55e15014f31</uuid></wall><wall><x>1441.0</x><y>190.0</y><rotation>0.20039855</rotation><width>65.30697</width><height>16.0</height><uuid>eb7cbc44-a682-4128-b06d-8b4fd5e0034a</uuid></wall><powerup><x>347.0</x><y>136.0</y><uuid>e2da427f-2143-4170-b9ac-caf5ea111949</uuid></powerup><powerup><x>688.0</x><y>197.0</y><uuid>d936837d-ba80-4a2a-9c75-e025fbdec83a</uuid></powerup><powerup><x>899.0</x><y>372.0</y><uuid>9363e2b9-787c-4955-92d6-e6207451ad4e</uuid></powerup><powerup><x>1212.0</x><y>506.0</y><uuid>1086808d-55c2-47a8-8766-16b804113c31</uuid></powerup><powerup><x>1633.0</x><y>498.0</y><uuid>3a598a8c-3c6c-4964-9683-5b3fba16ccec</uuid></powerup><powerup><x>1929.0</x><y>337.0</y><uuid>2418b93e-79fd-415f-8cb2-d0f61b874b64</uuid></powerup><powerup><x>2109.0</x><y>111.0</y><uuid>216506cf-4ae0-4146-a5a3-cd6c4688532d</uuid></powerup><powerup><x>2107.0</x><y>-216.0</y><uuid>9f2ce5d2-5a40-42d6-9052-1b09632dfcef</uuid></powerup><powerup><x>2141.0</x><y>-61.0</y><uuid>4706daa1-c094-4706-8f03-af6b61d86366</uuid></powerup><powerup><x>1511.0</x><y>-363.0</y><uuid>611d00b3-fe6d-459b-8063-ed90ca8dc3a7</uuid></powerup><powerup><x>1657.0</x><y>-457.0</y><uuid>32ec6fd2-1395-4253-ac26-fd0b4682c786</uuid></powerup><powerup><x>1842.0</x><y>-424.0</y><uuid>6d08ebaf-2093-45b3-a325-30a94e831880</uuid></powerup><powerup><x>1433.0</x><y>-209.0</y><uuid>83b6d906-1abc-46ff-88ed-5d6c31a4538c</uuid></powerup></root><root><missleOrigin><x>0.0</x><y>0.0</y><angle>0.7539824</angle></missleOrigin><earthOrigin><x>7931.0</x><y>1846.0</y></earthOrigin><isCameraFollowsMissle>true</isCameraFollowsMissle><fileName>Cursa-434</fileName><creatorId>057dbe61</creatorId><uuid>17a230a8-2286-422d-9a4e-58d769b39f3d</uuid><isbuiltin>false</isbuiltin><wall><x>-95.0</x><y>-97.5</y><rotation>5.2358108</rotation><width>256.0711</width><height>24.0</height><uuid>9d90cfc8-4c67-449e-9110-6920cafe2304</uuid></wall><wall><x>27.0</x><y>-115.5</y><rotation>0.9110131</rotation><width>232.82768</width><height>24.0</height><uuid>7ce5f312-69eb-4ffb-bed4-6d90fe4e2b73</uuid></wall><wall><x>142.5</x><y>-0.5</y><rotation>0.5629508</rotation><width>145.7949</width><height>24.0</height><uuid>4b9f443f-e08f-4f2e-81cd-0854101ad5ac</uuid></wall><wall><x>247.0</x><y>38.5</y><rotation>0.122032195</rotation><width>130.79419</width><height>24.0</height><uuid>ebc32d36-3c48-46b0-a335-36fb919219b5</uuid></wall><wall><x>364.5</x><y>40.0</y><rotation>6.20582</rotation><width>153.38702</width><height>24.0</height><uuid>5e9e9e47-235d-4831-8284-c5ab817c0e0d</uuid></wall><wall><x>489.0</x><y>9.5</y><rotation>5.8813148</rotation><width>154.38788</width><height>24.0</height><uuid>6ba89750-5eb8-46df-bb52-98e82708b1b0</uuid></wall><wall><x>603.5</x><y>-69.0</y><rotation>5.5117397</rotation><width>176.04276</width><height>24.0</height><uuid>9bf509fd-626e-44df-958f-493d41f1fe82</uuid></wall><wall><x>707.0</x><y>-190.0</y><rotation>5.336799</rotation><width>191.63054</width><height>24.0</height><uuid>7ebd5853-461e-434b-9124-bfc962a264eb</uuid></wall><wall><x>809.5</x><y>-303.5</y><rotation>5.57842</rotation><width>164.46352</width><height>24.0</height><uuid>9499d334-b279-475e-92a0-9508d7c1a1eb</uuid></wall><wall><x>913.0</x><y>-213.5</y><rotation>2.5380204</rotation><width>184.32779</width><height>24.0</height><uuid>8612a13b-b1ea-4d88-bf19-b45d2bf43412</uuid></wall><wall><x>812.0</x><y>-118.0</y><rotation>2.1815221</rotation><width>146.06555</width><height>24.0</height><uuid>ced28025-7925-4577-9eba-a1a309e4cd02</uuid></wall><wall><x>749.0</x><y>-9.0</y><rotation>2.0138988</rotation><width>154.61394</width><height>24.0</height><uuid>0d027ce7-6f7a-420f-84f9-6afe9dd9ab8f</uuid></wall><wall><x>681.0</x><y>101.5</y><rotation>2.2311668</rotation><width>154.41856</width><height>24.0</height><uuid>e36d256e-8371-4c1b-9505-02d21aa96336</uuid></wall><wall><x>567.0</x><y>207.5</y><rotation>2.5067966</rotation><width>207.80696</width><height>24.0</height><uuid>1dceaa53-aa82-4f85-b9ab-cc112b893f5f</uuid></wall><wall><x>442.0</x><y>249.0</y><rotation>3.3911796</rotation><width>129.26158</width><height>24.0</height><uuid>98f49e9c-03d6-4843-8a67-5227a07e528b</uuid></wall><wall><x>345.5</x><y>218.0</y><rotation>3.5183039</rotation><width>121.862144</width><height>24.0</height><uuid>0e5dd667-cc16-41ab-a89c-22d3bdc625bb</uuid></wall><wall><x>270.5</x><y>257.5</y><rotation>2.0448241</rotation><width>153.2517</width><height>24.0</height><uuid>85ae78d1-a087-4dba-935e-e3976d1d607f</uuid></wall><wall><x>170.5</x><y>295.5</y><rotation>3.4114416</rotation><width>170.29422</width><height>24.0</height><uuid>3bc5be94-49ad-4720-9975-5bcc2fc570eb</uuid></wall><wall><x>54.5</x><y>225.0</y><rotation>3.983924</rotation><width>160.69308</width><height>24.0</height><uuid>ab214885-3eb4-4991-93d1-44f8d0d5400b</uuid></wall><wall><x>-72.0</x><y>88.5</y><rotation>3.9540112</rotation><width>259.55255</width><height>24.0</height><uuid>5192a841-5d24-4d69-b5b6-8451a5a48e25</uuid></wall><wall><x>-153.0</x><y>3.0</y><rotation>1.5707964</rotation><width>24.0</width><height>24.0</height><uuid>a81c24ad-c1d3-49da-b384-3a990913a426</uuid></wall><wall><x>-153.0</x><y>3.0</y><rotation>1.5707964</rotation><width>24.0</width><height>24.0</height><uuid>c982e6ab-864e-4d25-abb3-c3548cfd3c2e</uuid></wall><wall><x>-153.0</x><y>3.0</y><rotation>1.5707964</rotation><width>24.0</width><height>24.0</height><uuid>7764a43a-2a1f-4e01-95e3-543cb498d64d</uuid></wall><wall><x>863.0</x><y>-349.0</y><rotation>1.5707964</rotation><width>24.0</width><height>24.0</height><uuid>2ea4811a-f7de-41dc-b8d7-5fc083a85030</uuid></wall><wall><x>956.5</x><y>-428.5</y><rotation>5.578536</rotation><width>269.45874</width><height>24.0</height><uuid>10490109-1e83-41c8-ba5a-c98ddc060a34</uuid></wall><wall><x>1143.5</x><y>-511.5</y><rotation>6.24577</rotation><width>211.13097</width><height>24.0</height><uuid>795e043f-7362-423b-8520-4074af9f4711</uuid></wall><wall><x>1306.5</x><y>-511.5</y><rotation>0.050317235</rotation><width>163.17615</width><height>24.0</height><uuid>fc5801b2-70c4-4d67-a548-70942506f550</uuid></wall><wall><x>1439.0</x><y>-477.0</y><rotation>0.45727822</rotation><width>164.42792</width><height>24.0</height><uuid>39ecbf2f-3f2d-4ec8-9cd9-6983f2a208ad</uuid></wall><wall><x>1545.5</x><y>-391.5</y><rotation>0.8971752</rotation><width>163.46326</width><height>24.0</height><uuid>f883a681-1ea7-47e3-9520-9ceace22a490</uuid></wall><wall><x>1591.5</x><y>-266.5</y><rotation>1.5353502</rotation><width>165.08862</width><height>24.0</height><uuid>dbb084da-ad89-474c-9f70-70f260b455ca</uuid></wall><wall><x>1591.5</x><y>-123.5</y><rotation>1.6052654</rotation><width>169.08618</width><height>24.0</height><uuid>ba5a777e-89a1-478c-b3e2-4824e8105039</uuid></wall><wall><x>1481.0</x><y>-28.0</y><rotation>2.9317644</rotation><width>244.84384</width><height>24.0</height><uuid>1a0f6252-524e-414c-9b49-97b9aef1c9bd</uuid></wall><wall><x>1366.5</x><y>-62.0</y><rotation>4.5988445</rotation><width>138.73883</width><height>24.0</height><uuid>2236ca8e-c7b0-44dd-b5a1-aa70c77cfd7e</uuid></wall><wall><x>1356.5</x><y>-167.0</y><rotation>4.639601</rotation><width>120.25487</width><height>24.0</height><uuid>424478b1-8775-4ce3-b710-242b8f631e1c</uuid></wall><wall><x>1302.5</x><y>-252.5</y><rotation>3.7803242</rotation><width>149.80142</width><height>24.0</height><uuid>38de318e-3e12-4588-b11c-c5c3b0409554</uuid></wall><wall><x>1182.0</x><y>-296.0</y><rotation>3.2270985</rotation><width>164.51335</width><height>24.0</height><uuid>f6c2add0-27c1-492b-8ee4-2c329509eb3e</uuid></wall><wall><x>1045.5</x><y>-280.5</y><rotation>2.8288918</rotation><width>163.7784</width><height>24.0</height><uuid>af4390c0-58ba-4d55-9da8-9542a526b950</uuid></wall><wall><x>979.0</x><y>-259.0</y><rotation>1.5707964</rotation><width>24.0</width><height>24.0</height><uuid>5e6ec648-1d3f-4509-b442-7def828f3dcf</uuid></wall><wall><x>1482.0</x><y>104.0</y><rotation>5.9546323</rotation><width>209.94623</width><height>24.0</height><uuid>5997f2ed-ebba-40bd-a0aa-91d113fd61f8</uuid></wall><wall><x>1573.5</x><y>159.5</y><rotation>1.5298835</rotation><width>195.14322</width><height>24.0</height><uuid>b6bbd034-936d-4954-9c87-972d80d6c02c</uuid></wall><wall><x>1582.0</x><y>309.5</y><rotation>1.4934317</rotation><width>153.38702</width><height>24.0</height><uuid>46fcb064-888c-4d78-9be3-97a5b97ff021</uuid></wall><wall><x>1641.0</x><y>413.0</y><rotation>0.625485</rotation><width>157.22162</width><height>24.0</height><uuid>187fa3dd-fd07-46d5-96a9-49b16205cd48</uuid></wall><wall><x>1771.5</x><y>450.5</y><rotation>6.263581</rotation><width>177.0294</width><height>24.0</height><uuid>72d3654f-c9a1-47c8-8eb1-617b9a01fec6</uuid></wall><wall><x>1911.0</x><y>421.5</y><rotation>5.871608</rotation><width>161.48091</width><height>24.0</height><uuid>a0d69e55-7ed6-476a-9839-1ca8ed869a61</uuid></wall><wall><x>1998.0</x><y>332.5</y><rotation>5.084457</rotation><width>156.03409</width><height>24.0</height><uuid>d891a2e6-4c9c-4523-a832-45259bd0c944</uuid></wall><wall><x>1997.5</x><y>202.0</y><rotation>4.3712025</rotation><width>170.44112</width><height>24.0</height><uuid>80ff66a5-a294-4091-af39-f5c43cf0825c</uuid></wall><wall><x>1906.0</x><y>62.0</y><rotation>3.9559681</rotation><width>219.24344</width><height>24.0</height><uuid>478035eb-681e-4c2e-a9d5-44188c1002b2</uuid></wall><wall><x>1807.5</x><y>-114.0</y><rotation>4.4209323</rotation><width>243.24643</width><height>24.0</height><uuid>4b91be18-c2df-4329-a5f3-4da42165a716</uuid></wall><wall><x>1824.0</x><y>-300.0</y><rotation>5.247344</rotation><width>212.30826</width><height>24.0</height><uuid>5e0ec538-b955-4f04-9c7c-073a0ac07466</uuid></wall><wall><x>1964.0</x><y>-419.5</y><rotation>5.8868513</rotation><width>223.46178</width><height>24.0</height><uuid>f77384ef-b96d-40d9-9c32-4da18274ca0b</uuid></wall><wall><x>2125.0</x><y>-459.0</y><rotation>6.268692</rotation><width>162.0145</width><height>24.0</height><uuid>2aa73a5b-2737-4c69-92ab-3f377889106a</uuid></wall><wall><x>2281.5</x><y>-439.0</y><rotation>0.23554493</rotation><width>203.96944</width><height>24.0</height><uuid>0dc3713e-ab47-4501-b55e-4ae277fbf1bb</uuid></wall><wall><x>2442.5</x><y>-352.0</y><rotation>0.7316864</rotation><width>221.5677</width><height>24.0</height><uuid>979654c8-a6a1-4887-90c3-4b9197421bcd</uuid></wall><wall><x>2553.5</x><y>-191.5</y><rotation>1.1930296</rotation><width>227.33716</width><height>24.0</height><uuid>10632910-62e7-4c17-8f11-cf4f0994265c</uuid></wall><wall><x>2584.0</x><y>10.0</y><rotation>1.6361238</rotation><width>238.45746</width><height>24.0</height><uuid>15787675-2f5b-4e39-8b41-cd90ea82510b</uuid></wall><wall><x>2569.0</x><y>212.5</y><rotation>1.6543708</rotation><width>215.66899</width><height>24.0</height><uuid>d08f07bf-cd70-4701-8225-905ac4ce1d6a</uuid></wall><wall><x>2471.5</x><y>321.0</y><rotation>2.9973497</rotation><width>204.87842</width><height>24.0</height><uuid>f976ac97-26a6-4488-a888-e640d5c0ad13</uuid></wall><wall><x>2385.0</x><y>236.0</y><rotation>4.7429914</rotation><width>220.09181</width><height>24.0</height><uuid>6835bdde-3c3c-4c9d-b079-915ab6c04520</uuid></wall><wall><x>2401.5</x><y>45.0</y><rotation>4.8565435</rotation><width>211.94946</width><height>24.0</height><uuid>2c4b77d0-56b8-4da1-a19f-0fec3b2b35b5</uuid></wall><wall><x>2373.5</x><y>-125.0</y><rotation>4.2180605</rotation><width>198.94284</width><height>24.0</height><uuid>797a7ef1-a892-4571-ad27-fab3fcbc0f95</uuid></wall><wall><x>2252.5</x><y>-236.0</y><rotation>3.5457249</rotation><width>196.93062</width><height>24.0</height><uuid>07f646bf-17ba-4534-8421-61af5f995d9d</uuid></wall><wall><x>2106.0</x><y>-250.5</y><rotation>2.8583717</rotation><width>163.56003</width><height>24.0</height><uuid>67653b87-cdef-4960-b43e-9751e826ac7f</uuid></wall><wall><x>2063.0</x><y>-150.5</y><rotation>1.2810498</rotation><width>192.00298</width><height>24.0</height><uuid>033a1ea0-54e3-4521-b874-2cc67e20330c</uuid></wall><wall><x>2143.5</x><y>8.5</y><rotation>0.94694114</rotation><width>217.43733</width><height>24.0</height><uuid>7793c1c9-7cb6-420c-bc9e-21b735c3986b</uuid></wall><wall><x>2217.0</x><y>177.5</y><rotation>1.3851148</rotation><width>208.16568</width><height>24.0</height><uuid>fc563f0d-e4e3-4a8f-9e2f-8bf81c249aef</uuid></wall><wall><x>2193.5</x><y>357.0</y><rotation>1.997847</rotation><width>219.5633</width><height>24.0</height><uuid>c00b27d8-6888-4709-8313-935a0602441e</uuid></wall><wall><x>2100.5</x><y>509.5</y><rotation>2.2616496</rotation><width>188.78471</width><height>24.0</height><uuid>a78631f1-cf94-4ff4-9d2e-7cb19eb9256e</uuid></wall><wall><x>1962.5</x><y>610.5</y><rotation>2.7282624</rotation><width>210.7244</width><height>24.0</height><uuid>8bbf98df-f19b-4648-b805-f343d9ee0d31</uuid></wall><wall><x>1784.0</x><y>637.5</y><rotation>3.2540197</rotation><width>211.18173</width><height>24.0</height><uuid>33254901-3916-453e-a0de-a916e47d1f9d</uuid></wall><wall><x>1579.5</x><y>578.0</y><rotation>3.5556486</rotation><width>267.58368</width><height>24.0</height><uuid>1ed25cec-f817-4101-8920-b9e2e78b39a3</uuid></wall><wall><x>1413.0</x><y>430.0</y><rotation>4.2052903</rotation><width>250.50386</width><height>24.0</height><uuid>5dabf01d-7751-4f93-97d4-e2612041fe74</uuid></wall><wall><x>1376.0</x><y>232.5</y><rotation>4.8931355</rotation><width>224.26233</width><height>24.0</height><uuid>543a91eb-852a-4eba-bdc6-9a4afe908040</uuid></wall><wall><x>1394.0</x><y>134.0</y><rotation>1.5707964</rotation><width>24.0</width><height>24.0</height><uuid>b9699093-83b1-4d59-afb2-92fd8460c2d8</uuid></wall><wall><x>1050.0</x><y>-508.0</y><rotation>1.5707964</rotation><width>24.0</width><height>24.0</height><uuid>721710f4-0efc-48b6-8c73-e5ff5bb3bf0a</uuid></wall><wall><x>1050.0</x><y>-508.0</y><rotation>1.5707964</rotation><width>24.0</width><height>24.0</height><uuid>669e8830-f02d-4091-a012-319b0721a737</uuid></wall><wall><x>1050.0</x><y>-508.0</y><rotation>1.5707964</rotation><width>24.0</width><height>24.0</height><uuid>4441508a-3d65-4209-859b-6efc50e8ae80</uuid></wall><wall><x>2467.5</x><y>407.0</y><rotation>6.087154</rotation><width>167.75327</width><height>24.0</height><uuid>a204b299-2930-45c9-81e6-88a2ad3884dd</uuid></wall><wall><x>2548.0</x><y>463.5</y><rotation>1.4298923</rotation><width>166.41138</width><height>24.0</height><uuid>4e6e9d48-3879-4280-b38a-ea92d0b0b047</uuid></wall><wall><x>2602.0</x><y>607.5</y><rotation>1.0313779</rotation><width>195.32718</width><height>24.0</height><uuid>f253e460-9be2-41dd-bc93-207d621ac6e6</uuid></wall><wall><x>2688.5</x><y>753.5</y><rotation>1.0405806</rotation><width>192.07736</width><height>24.0</height><uuid>b0e16b40-48ac-4ab0-8b4e-10322f2924f5</uuid></wall><wall><x>2904.5</x><y>830.0</y><rotation>0.023048213</rotation><width>371.0922</width><height>24.0</height><uuid>678c1652-882f-49ef-b248-0f70216f9dd0</uuid></wall><wall><x>3148.5</x><y>604.0</y><rotation>5.009818</rotation><width>505.12473</width><height>24.0</height><uuid>ddd0ec29-1d5e-47b4-b452-45f920018a07</uuid></wall><wall><x>3345.5</x><y>713.0</y><rotation>1.8501085</rotation><width>702.2868</width><height>24.0</height><uuid>4509d457-adc8-418c-8b5a-045a56e4b8a1</uuid></wall><wall><x>2941.0</x><y>1042.0</y><rotation>3.13195</rotation><width>646.02893</width><height>24.0</height><uuid>6bc32987-9040-4f4f-be89-163693ea6596</uuid></wall><wall><x>2527.0</x><y>1005.5</y><rotation>3.50779</rotation><width>244.62865</width><height>24.0</height><uuid>525d4187-dee8-4766-9551-308647829b56</uuid></wall><wall><x>2386.5</x><y>886.5</y><rotation>4.2716384</rotation><width>199.80103</width><height>24.0</height><uuid>f93c1c51-4696-4dd7-9f6b-c727dee66ed2</uuid></wall><wall><x>2349.0</x><y>697.5</y><rotation>4.712389</rotation><width>243.0</width><height>24.0</height><uuid>e055196b-9aaf-4688-b13d-90451b764a3a</uuid></wall><wall><x>2349.0</x><y>588.0</y><rotation>1.5707964</rotation><width>24.0</width><height>24.0</height><uuid>4893d1cd-bb5c-4668-9410-fa964dca8d5b</uuid></wall><wall><x>2373.0</x><y>504.5</y><rotation>4.9922695</rotation><width>197.76134</width><height>24.0</height><uuid>28902102-e367-447c-a407-fcca2845b6ff</uuid></wall><wall><x>2397.0</x><y>421.0</y><rotation>1.5707964</rotation><width>24.0</width><height>24.0</height><uuid>a05de4ce-6d14-403b-aa0e-956d08e4c8d0</uuid></wall><wall><x>3219.0</x><y>374.0</y><rotation>1.5707964</rotation><width>24.0</width><height>24.0</height><uuid>c1f8e534-d693-4c7d-b490-c7462a54d8a7</uuid></wall><wall><x>3242.0</x><y>251.0</y><rotation>4.897246</rotation><width>274.26385</width><height>24.0</height><uuid>cf2e1e3c-e121-4607-8eda-5c228fb69733</uuid></wall><wall><x>3292.0</x><y>75.5</y><rotation>5.1874</rotation><width>142.07202</width><height>24.0</height><uuid>e5f114e2-71a1-4ae0-aca4-d9cb3f94084e</uuid></wall><wall><x>3377.5</x><y>10.0</y><rotation>6.0645165</rotation><width>143.85408</width><height>24.0</height><uuid>abc13700-26f6-4734-85af-b0c88a4ffffa</uuid></wall><wall><x>3867.5</x><y>64.0</y><rotation>0.15404221</rotation><width>897.3413</width><height>24.0</height><uuid>b80d3980-5306-42d7-87d5-5f6ce5994cda</uuid></wall><wall><x>4334.0</x><y>165.5</y><rotation>0.77820396</rotation><width>122.29038</width><height>24.0</height><uuid>29b3e17f-6794-4414-a397-3e597655e8eb</uuid></wall><wall><x>4272.0</x><y>542.5</y><rotation>1.8467808</rotation><width>735.9417</width><height>24.0</height><uuid>5b1492a0-df53-4aab-97c3-6660e1b17d20</uuid></wall><wall><x>4033.5</x><y>843.0</y><rotation>3.430129</rotation><width>319.2033</width><height>24.0</height><uuid>b951fd37-1e92-4d9e-8725-5fe99ffb0ad3</uuid></wall><wall><x>3980.5</x><y>554.5</y><rotation>5.057082</rotation><width>547.81104</width><height>24.0</height><uuid>222d61a6-f7e9-4579-9232-e0254bf6f5be</uuid></wall><wall><x>3806.0</x><y>290.0</y><rotation>3.2099278</rotation><width>551.2305</width><height>24.0</height><uuid>77159c4e-1138-4c9b-82a9-e58100b83cab</uuid></wall><wall><x>3491.0</x><y>329.5</y><rotation>2.3060083</rotation><width>179.0516</width><height>24.0</height><uuid>da8a65c0-06a1-4ef4-8a0f-6da5dd4ac46a</uuid></wall><wall><x>3439.0</x><y>387.0</y><rotation>1.5707964</rotation><width>24.0</width><height>24.0</height><uuid>113da81b-8cba-4d84-88ee-d468e52c8c6b</uuid></wall><wall><x>4069.0</x><y>308.0</y><rotation>1.5707964</rotation><width>24.0</width><height>24.0</height><uuid>67be35df-a9ab-4941-8a3c-5bed6a82653f</uuid></wall><wall><x>3966.0</x><y>298.5</y><rotation>3.2335653</rotation><width>230.87436</width><height>24.0</height><uuid>d7c0db98-7e12-4201-b429-dfd30db5afb5</uuid></wall><wall><x>3699.5</x><y>645.5</y><rotation>2.0008001</rotation><width>808.4093</width><height>24.0</height><uuid>697063f8-7f76-493e-8ab3-ee2709835199</uuid></wall><wall><x>3880.5</x><y>1074.5</y><rotation>0.20742333</rotation><width>728.09235</width><height>24.0</height><uuid>31d67079-2252-47f9-a51c-c4c8479c6cbb</uuid></wall><wall><x>4284.5</x><y>1128.5</y><rotation>5.981736</rotation><width>148.61942</width><height>24.0</height><uuid>ba40287b-4f7b-4735-8036-1b69522c63b6</uuid></wall><wall><x>4388.0</x><y>1047.0</y><rotation>5.322049</rotation><width>177.68799</width><height>24.0</height><uuid>040227d6-2781-4c5f-a6d8-683c5d3c1aef</uuid></wall><wall><x>4501.5</x><y>688.0</y><rotation>4.943009</rotation><width>632.0995</width><height>24.0</height><uuid>e55c9d1e-ddba-4a6b-b2dd-6b3063b5ca0f</uuid></wall><wall><x>4576.5</x><y>217.5</y><rotation>0.12642075</rotation><width>444.3546</width><height>24.0</height><uuid>8a34686c-d759-4c9e-8dce-d3ea249148eb</uuid></wall><wall><x>4896.5</x><y>247.5</y><rotation>0.03137882</rotation><width>247.10983</width><height>24.0</height><uuid>9ed2e3c1-09c2-40ec-9edd-278a6c0c870f</uuid></wall><wall><x>5151.0</x><y>227.5</y><rotation>6.120306</rotation><width>313.83615</width><height>24.0</height><uuid>75b2cad0-8488-4e91-8e98-548f78ec8543</uuid></wall><wall><x>5443.0</x><y>150.0</y><rotation>5.9354925</rotation><width>340.9669</width><height>24.0</height><uuid>bc3ac4f5-49c9-4a3c-a2e2-16f8310289ec</uuid></wall><wall><x>5771.0</x><y>80.0</y><rotation>6.1940374</rotation><width>383.4273</width><height>24.0</height><uuid>8d57cf60-f5f7-41b4-86ba-3d4c21cb13c0</uuid></wall><wall><x>4746.0</x><y>415.5</y><rotation>0.20824602</rotation><width>367.41956</width><height>24.0</height><uuid>ee1d07e2-fea8-46f1-9955-e9adac3e8d94</uuid></wall><wall><x>5010.5</x><y>452.5</y><rotation>0.015544841</rotation><width>217.02332</width><height>24.0</height><uuid>58b48f68-5993-4c4f-9336-547c144d5dcb</uuid></wall><wall><x>5214.0</x><y>420.5</y><rotation>5.9797688</rotation><width>248.24316</width><height>24.0</height><uuid>a72e35b8-1b19-4cb8-94b6-be4a4e130866</uuid></wall><wall><x>5465.5</x><y>326.5</y><rotation>5.8866754</rotation><width>337.30817</width><height>24.0</height><uuid>0e09baf3-9227-4e33-adb5-0b6bd61c9fd1</uuid></wall><wall><x>5789.5</x><y>258.5</y><rotation>6.2414265</rotation><width>383.31323</width><height>24.0</height><uuid>0c91511d-f049-4a6c-82b7-65d01d623c96</uuid></wall><wall><x>5969.0</x><y>251.0</y><rotation>1.5707964</rotation><width>24.0</width><height>24.0</height><uuid>d2f34c53-19cc-4c00-9435-a30de64cbbf0</uuid></wall><wall><x>6130.5</x><y>256.0</y><rotation>0.030948406</rotation><width>347.15475</width><height>24.0</height><uuid>002d5922-aaf1-4b5d-b7cc-d6de6eeb8c3d</uuid></wall><wall><x>6437.5</x><y>301.0</y><rotation>0.26828644</rotation><width>325.7963</width><height>24.0</height><uuid>a6767f15-9110-4142-bc5e-e68ad5b2a4ae</uuid></wall><wall><x>6679.5</x><y>432.0</y><rotation>0.75607324</rotation><width>289.27908</width><height>24.0</height><uuid>99e08c7e-5215-4238-ac99-6f23d8d3c298</uuid></wall><wall><x>5950.0</x><y>64.0</y><rotation>1.5707964</rotation><width>24.0</width><height>24.0</height><uuid>68126b6f-0794-477b-be7a-1c4f009da90c</uuid></wall><wall><x>6129.0</x><y>58.5</y><rotation>6.252469</rotation><width>382.16895</width><height>24.0</height><uuid>fd6ea0b2-f192-4a14-b75d-581ddaec631f</uuid></wall><wall><x>6498.0</x><y>119.0</y><rotation>0.3343285</rotation><width>426.27353</width><height>24.0</height><uuid>95d8e1f6-7a33-4cae-be08-51f80be4b02b</uuid></wall><wall><x>6828.5</x><y>355.5</y><rotation>0.88156366</rotation><width>465.86197</width><height>24.0</height><uuid>cee11cb8-452e-4c2b-9d13-5f943886fbc3</uuid></wall><wall><x>6969.0</x><y>526.0</y><rotation>1.5707964</rotation><width>24.0</width><height>24.0</height><uuid>df1fdd21-92d1-4bb1-b681-adbf8084e967</uuid></wall><wall><x>6972.5</x><y>776.0</y><rotation>1.5567973</rotation><width>524.049</width><height>24.0</height><uuid>95ed646f-956d-4f98-bc85-f9d348410b29</uuid></wall><wall><x>7163.5</x><y>1036.0</y><rotation>0.05328269</rotation><width>399.53296</width><height>24.0</height><uuid>b30cce98-c2e5-4c37-9e02-26688d1bc289</uuid></wall><wall><x>7345.5</x><y>1256.0</y><rotation>1.5969808</rotation><width>444.144</width><height>24.0</height><uuid>5527cd0b-9029-49d6-9a33-cbc2b3711e41</uuid></wall><wall><x>6776.0</x><y>523.0</y><rotation>1.5707964</rotation><width>24.0</width><height>24.0</height><uuid>4c7071b5-0fd0-46cf-8338-e9cfdca237c1</uuid></wall><wall><x>6795.5</x><y>865.5</y><rotation>1.5139234</rotation><width>710.1093</width><height>24.0</height><uuid>51f0818b-c3e2-4ec1-b543-c7092624f149</uuid></wall><wall><x>6981.0</x><y>1239.0</y><rotation>0.1846201</rotation><width>361.73953</width><height>24.0</height><uuid>01be4358-7b57-4580-9610-36328d7c7e3b</uuid></wall><wall><x>7147.0</x><y>1270.0</y><rotation>1.5707964</rotation><width>24.0</width><height>24.0</height><uuid>aaffff44-dcf0-4165-a7f8-342d725abc3c</uuid></wall><wall><x>7142.5</x><y>1470.5</y><rotation>1.5932364</rotation><width>425.10098</width><height>24.0</height><uuid>5c807e3d-050e-4d70-8ecc-019126742b38</uuid></wall><wall><x>7180.0</x><y>1758.5</y><rotation>1.1232764</rotation><width>218.11595</width><height>24.0</height><uuid>b1c72d02-06a3-4e62-a585-32972640af30</uuid></wall><wall><x>7296.5</x><y>1904.0</y><rotation>0.66150767</rotation><width>212.83061</width><height>24.0</height><uuid>426e0c12-ed68-441b-93e6-7981f251ab2a</uuid></wall><wall><x>7696.5</x><y>1967.5</y><rotation>0.01689713</rotation><width>675.09296</width><height>24.0</height><uuid>03bd17a9-32e7-479f-9f04-d334bfc624d3</uuid></wall><wall><x>7340.0</x><y>1466.0</y><rotation>1.5707964</rotation><width>24.0</width><height>24.0</height><uuid>ed543e3d-22b8-4acd-936f-ca378010dacc</uuid></wall><wall><x>7375.5</x><y>1575.0</y><rotation>1.2559422</rotation><width>253.27058</width><height>24.0</height><uuid>e65b8fc2-f3b9-43e3-9331-e0e34dda8804</uuid></wall><wall><x>7716.0</x><y>1700.5</y><rotation>0.0540461</rotation><width>634.89197</width><height>24.0</height><uuid>13621232-142f-458d-b0fc-c5a6bf4859a4</uuid></wall><wall><x>5950.0</x><y>64.0</y><rotation>1.5707964</rotation><width>24.0</width><height>24.0</height><uuid>b7ff5e7c-a123-4797-b247-a0faa5dbef51</uuid></wall><wall><x>5592.0</x><y>96.0</y><rotation>1.5707964</rotation><width>24.0</width><height>24.0</height><uuid>7135859b-8595-4852-9ac9-4214e9f0df7a</uuid></wall><wall><x>8022.0</x><y>1973.0</y><rotation>1.5707964</rotation><width>24.0</width><height>24.0</height><uuid>bdfbacff-1732-41a9-b656-675b30374f99</uuid></wall><wall><x>8021.5</x><y>1845.0</y><rotation>4.7084827</rotation><width>280.00195</width><height>24.0</height><uuid>0d6fd21e-c847-44f7-a4b1-e5e3b3c6dc11</uuid></wall><wall><x>8021.0</x><y>1717.0</y><rotation>1.5707964</rotation><width>24.0</width><height>24.0</height><uuid>57721c74-a4a9-4ada-ab2b-004b31b22f72</uuid></wall><powerup><x>470.0</x><y>189.0</y><uuid>2dfc0795-dc15-49ac-9db5-9002681bcf7e</uuid></powerup><powerup><x>1607.0</x><y>526.0</y><uuid>df9dfcad-a57f-4970-9ba8-70b842b8245d</uuid></powerup><powerup><x>1981.0</x><y>493.0</y><uuid>72b0cfeb-b192-45b4-8109-11c1752beae5</uuid></powerup><powerup><x>2108.0</x><y>209.0</y><uuid>ab83af50-e367-459c-8e7e-c80386854afc</uuid></powerup><powerup><x>1920.0</x><y>-169.0</y><uuid>169282b4-e610-4c99-a38e-f67bc255aa5c</uuid></powerup><powerup><x>2048.0</x><y>-386.0</y><uuid>779c28a1-8424-4945-9610-1ea138b3c57a</uuid></powerup><powerup><x>2400.0</x><y>-249.0</y><uuid>4f3919f9-3afd-4f28-8d5a-6df389cefd3e</uuid></powerup><powerup><x>2486.0</x><y>53.0</y><uuid>2cf91319-b76f-4372-9214-8cb75b7675a9</uuid></powerup><powerup><x>2565.8457</x><y>742.9861</y><uuid>3c5491bb-6de1-40f3-9f5f-49c82b88bf1c</uuid></powerup><powerup><x>2643.0</x><y>911.0</y><uuid>a9858836-5090-47e4-a88c-44a1ec2fcfe0</uuid></powerup><powerup><x>2930.0</x><y>942.0</y><uuid>b7ba0c30-81cf-4688-ba8e-b586e4fe946c</uuid></powerup><powerup><x>3272.0</x><y>826.0</y><uuid>dad97c32-81e0-416d-b32f-7a5feda3d8b1</uuid></powerup><powerup><x>3319.0</x><y>493.0</y><uuid>c43fcdc6-95d5-4f98-8370-599cad222f03</uuid></powerup><powerup><x>3533.0</x><y>155.0</y><uuid>4e2d12a7-8278-433d-9cfe-aff57e75a991</uuid></powerup><powerup><x>4132.0</x><y>209.0</y><uuid>00a38635-e181-4733-bb3f-8e22336a1b5e</uuid></powerup><powerup><x>4256.0</x><y>322.0</y><uuid>7e2492eb-ecc0-4fce-a01d-29c228298724</uuid></powerup><powerup><x>7536.0</x><y>1843.0</y><uuid>cf66ff80-0f64-4f87-b2d9-a26b213819d5</uuid></powerup><powerup><x>7276.0</x><y>1695.0</y><uuid>cb933d3a-5e90-4ec6-900d-7496f86a1a3d</uuid></powerup><powerup><x>7234.0</x><y>1379.0</y><uuid>18e0fd05-353e-4598-807b-e07a79b22be4</uuid></powerup><powerup><x>7116.0</x><y>1131.0</y><uuid>b0599f07-d840-40ff-bf7e-6274a9e1d7ab</uuid></powerup><powerup><x>6883.0</x><y>1034.0</y><uuid>e818a5ad-3dcb-44ce-9ec0-dd6fb883d3ef</uuid></powerup><powerup><x>6886.0</x><y>807.0</y><uuid>52d3e85d-a124-4efc-adde-0dab7a3d7e17</uuid></powerup><powerup><x>6879.0</x><y>570.0</y><uuid>2cd1979c-300d-49ff-9ded-3e6f4f091100</uuid></powerup><powerup><x>6555.0</x><y>246.0</y><uuid>5a22f472-a28a-491b-8fb5-53d40e5c1a9f</uuid></powerup><powerup><x>6302.0</x><y>148.0</y><uuid>e62cc49b-e1e0-4223-87d4-ab62cfc34dc4</uuid></powerup><powerup><x>5957.0</x><y>159.0</y><uuid>b681cf66-46f6-4aea-b8a8-9f82bf082cfe</uuid></powerup><powerup><x>5420.0</x><y>251.0</y><uuid>0f321201-8e2d-4d1a-bd17-5e709630f681</uuid></powerup><powerup><x>5177.0</x><y>330.0</y><uuid>62889e61-5047-4eef-a76d-20b759b21fd5</uuid></powerup><powerup><x>4845.0</x><y>340.0</y><uuid>a9e8c779-7990-41be-964b-11039019530f</uuid></powerup><powerup><x>4418.0</x><y>464.0</y><uuid>ae47c088-d577-4d30-bbc8-3ffe6cacef65</uuid></powerup><powerup><x>4421.0</x><y>769.0</y><uuid>12100771-4620-4bc4-a807-775ebcc402f8</uuid></powerup><powerup><x>4190.0</x><y>1021.0</y><uuid>fa7ae4a4-692e-455b-b83c-cf258a16d430</uuid></powerup><powerup><x>3882.0</x><y>963.0</y><uuid>85ad6c66-d27e-4aa9-9a3b-bad7463d4f19</uuid></powerup><powerup><x>3752.0</x><y>851.0</y><uuid>d321603e-5b45-491f-af91-3677f31ab0b2</uuid></powerup><warp><x>1468.0</x><y>-113.0</y><rotation>0.0</rotation><name>WARP-0c96c3</name><connectsTo>WARP-07f6e2</connectsTo><uuid>30c96c3a-60a5-4bff-9202-2b304c1691df</uuid></warp><warp><x>1486.0</x><y>213.0</y><rotation>0.0</rotation><name>WARP-07f6e2</name><connectsTo>WARP-07f6e2</connectsTo><uuid>d07f6e28-20ee-45bc-87b4-8b387cf06ebc</uuid></warp><warp><x>2467.0</x><y>245.0</y><rotation>0.0</rotation><name>WARP-a7240c</name><connectsTo>WARP-3f9cad</connectsTo><uuid>da7240cb-0b3b-4773-8eca-59f9af6ec7e6</uuid></warp><warp><x>2454.3333</x><y>531.5227</y><rotation>0.0</rotation><name>WARP-3f9cad</name><connectsTo>WARP-3f9cad</connectsTo><uuid>b3f9cade-debd-4243-96a9-e67b9ca9a512</uuid></warp><warp><x>4059.0</x><y>731.0</y><rotation>0.0</rotation><name>WARP-755493</name><connectsTo>WARP-dd707c</connectsTo><uuid>a755493c-2286-440d-8ce9-f6c4a0ef9202</uuid></warp><warp><x>3922.607</x><y>411.23862</y><rotation>0.0</rotation><name>WARP-dd707c</name><connectsTo>WARP-dd707c</connectsTo><uuid>2dd707ce-016e-4eda-8a10-7d2bfc5c1e0e</uuid></warp></root><root><missleOrigin><x>-6.25</x><y>-36.595703</y><angle>-0.06283185</angle></missleOrigin><earthOrigin><x>200.0</x><y>200.0</y></earthOrigin><isCameraFollowsMissle>true</isCameraFollowsMissle><fileName>Nashira-146</fileName><creatorId>03806184439f9297</creatorId><uuid>f3176dee-248f-4a24-bc80-8e73c91ffcfc</uuid><isbuiltin>false</isbuiltin><wall><x>116.75003</x><y>57.65985</y><rotation>-0.07339732</rotation><width>409.10144</width><height>16.0</height><uuid>f3176dee-248f-4a24-bc80-8e73c91ffcfc</uuid></wall><wall><x>116.0</x><y>-145.0</y><rotation>-0.07541227</rotation><width>451.28262</width><height>16.0</height><uuid>9f5fec4e-59f1-4e67-9391-fd49dd776e81</uuid></wall><wall><x>-90.125</x><y>-28.510727</y><rotation>-1.6748962</rotation><width>202.09404</width><height>16.0</height><uuid>d84ac7db-a3bb-425e-98c6-4a025136705b</uuid></wall><wall><x>401.0</x><y>126.06439</y><rotation>0.7853982</rotation><width>244.65895</width><height>16.0</height><uuid>83ac73d2-491f-4387-aa1a-570fa9d7d980</uuid></wall><wall><x>505.375</x><y>8.617096</y><rotation>0.79552805</rotation><width>488.63586</width><height>16.0</height><uuid>acd70cae-8230-4724-93e5-8e5e9152c174</uuid></wall><wall><x>492.125</x><y>281.4256</y><rotation>1.4532847</rotation><width>145.0</width><height>16.0</height><uuid>1582f0ab-5387-45fc-9877-cf0a230df128</uuid></wall><wall><x>861.5</x><y>176.10603</y><rotation>-0.026309717</rotation><width>380.13156</width><height>16.0</height><uuid>4900d444-28d1-48d6-a110-a0e3e14f0521</uuid></wall><wall><x>780.875</x><y>347.80887</y><rotation>-0.043300476</rotation><width>577.5413</width><height>16.0</height><uuid>75a5c6c9-0d40-43d6-bcea-8e28b2744559</uuid></wall><wall><x>1242.5</x><y>279.83026</y><rotation>-0.30723408</rotation><width>370.34174</width><height>16.0</height><uuid>8bad1b8e-2125-4795-b4ad-fabcfe84264e</uuid></wall><wall><x>1216.25</x><y>131.10626</y><rotation>-0.2342877</rotation><width>340.29694</width><height>16.0</height><uuid>7dc08a23-e65a-4aab-8b25-c5cd93ba00ac</uuid></wall><wall><x>1688.75</x><y>341.10712</y><rotation>0.4022556</rotation><width>597.709</width><height>16.0</height><uuid>ec3ddf3c-6c29-4905-b11b-5ecf98c592fc</uuid></wall><wall><x>1488.25</x><y>86.744705</y><rotation>-0.0414509</rotation><width>217.18655</width><height>16.0</height><uuid>ea127c88-059e-42e8-be09-4b296631d37b</uuid></wall><wall><x>1816.375</x><y>164.61707</y><rotation>0.35144478</rotation><width>479.29636</width><height>16.0</height><uuid>72704658-efe9-4de0-9220-c35f915afb52</uuid></wall><wall><x>2167.25</x><y>192.53195</y><rotation>-0.38965046</rotation><width>284.31146</width><height>16.0</height><uuid>d3be89da-384d-45aa-bc5e-01a919ab58c7</uuid></wall><wall><x>2157.25</x><y>391.0426</y><rotation>-0.32250413</rotation><width>419.63437</width><height>16.0</height><uuid>cbf1467a-16d7-460d-b26b-bc7de82af69d</uuid></wall><wall><x>2489.75</x><y>327.21283</y><rotation>0.01831297</rotation><width>273.04578</width><height>16.0</height><uuid>3318e703-8f09-45bd-9d7c-78d5e86af9b9</uuid></wall><wall><x>2448.5</x><y>135.72345</y><rotation>-0.026137836</rotation><width>306.10455</width><height>16.0</height><uuid>52e2cfa0-8cd7-47d6-971e-a97b5975c786</uuid></wall><wall><x>2717.375</x><y>415.7873</y><rotation>0.7396081</rotation><width>262.61188</width><height>16.0</height><uuid>ee31aeff-f884-4dec-a6de-b10b24f3a699</uuid></wall><wall><x>2884.25</x><y>343.02133</y><rotation>0.7359966</rotation><width>443.89752</width><height>16.0</height><uuid>b85f8375-f283-4a49-b490-0351d37ce34d</uuid></wall><wall><x>2658.625</x><y>162.38303</y><rotation>0.47951928</rotation><width>140.89003</width><height>16.0</height><uuid>c5a1eb4d-3208-4cdc-aba1-f60844226adb</uuid></wall><wall><x>2684.125</x><y>598.766</y><rotation>2.4714224</rotation><width>307.50934</width><height>16.0</height><uuid>03fef091-0b58-4725-bcb8-5af32652428b</uuid></wall><wall><x>3049.0</x><y>572.0</y><rotation>-1.5650821</rotation><width>175.00285</width><height>16.0</height><uuid>29608a6a-5e2e-4b9b-93a3-c2cf3b3f4e81</uuid></wall><wall><x>2886.0</x><y>760.2554</y><rotation>2.5746691</rotation><width>394.7569</width><height>16.0</height><uuid>3c099df7-ad34-4409-a2ba-fa2ec9611ae6</uuid></wall><wall><x>2611.5</x><y>893.1915</y><rotation>2.8987348</rotation><width>228.71161</width><height>16.0</height><uuid>25e1b84d-6ad2-44a4-a93f-0dd2f804da2f</uuid></wall><wall><x>2472.125</x><y>672.34045</y><rotation>-2.941194</rotation><width>195.9209</width><height>16.0</height><uuid>65f51b30-e9f0-4a77-bfe0-ba124e96c7b9</uuid></wall><wall><x>2146.875</x><y>808.9149</y><rotation>-2.8383896</rotation><width>750.2213</width><height>16.0</height><uuid>5ac39019-4b43-4aa9-9863-573bc1410110</uuid></wall><wall><x>2103.75</x><y>580.4043</y><rotation>-2.8846283</rotation><width>566.60394</width><height>16.0</height><uuid>bd3553df-a5c6-4b1d-b7b5-0016a044cc0a</uuid></wall><wall><x>1518.625</x><y>700.06384</y><rotation>3.1325185</rotation><width>551.0227</width><height>16.0</height><uuid>ea2ea7fe-7096-4577-b496-e597bb4bead0</uuid></wall><wall><x>1535.5</x><y>506.6596</y><rotation>-3.1365507</rotation><width>595.00757</width><height>16.0</height><uuid>e1bd21bd-9567-4950-b928-e4eb891708a9</uuid></wall><wall><x>1183.125</x><y>740.4043</y><rotation>2.5758913</rotation><width>149.25146</width><height>16.0</height><uuid>f55d2062-5a90-4b4e-a7a0-a0341e6a3e4a</uuid></wall><wall><x>1125.625</x><y>582.10645</y><rotation>2.5466595</rotation><width>280.13034</width><height>16.0</height><uuid>f3178df6-8dc4-4d40-a13f-8d5001a6735b</uuid></wall><wall><x>765.875</x><y>653.149</y><rotation>-3.1194189</rotation><width>496.12195</width><height>16.0</height><uuid>6d858bbc-4699-404c-8c76-072a29d8339d</uuid></wall><wall><x>824.0</x><y>778.2554</y><rotation>-3.1382482</rotation><width>598.00336</width><height>16.0</height><uuid>b1fd9374-ef96-47f7-93f9-f8f4195a477e</uuid></wall><wall><x>512.5</x><y>507.27664</y><rotation>-1.6449842</rotation><width>296.81644</width><height>16.0</height><uuid>565f6a41-66fd-4fe4-872d-0cd8cf513be1</uuid></wall><wall><x>281.0</x><y>705.0</y><rotation>-2.8614595</rotation><width>524.44354</width><height>16.0</height><uuid>5a01c3c2-011e-4b9e-b152-f4d707c1aecb</uuid></wall><wall><x>-54.625</x><y>279.0639</y><rotation>1.4498022</rotation><width>405.968</width><height>16.0</height><uuid>97979d10-b6d1-4c22-acc9-653388d91b1a</uuid></wall><wall><x>-0.5</x><y>551.70215</y><rotation>1.1852293</rotation><width>183.46935</width><height>16.0</height><uuid>1f988e3a-3cdd-4ce7-ad31-3479e6c7e688</uuid></wall><powerup><x>319.0</x><y>-54.0</y><uuid>da11a39b-3bca-4056-a9e2-95deeef89a3d</uuid></powerup><powerup><x>613.0</x><y>254.0</y><uuid>8b9bb567-6a3a-4ed3-81cf-d8656807e508</uuid></powerup><powerup><x>1111.0</x><y>241.0</y><uuid>00144aba-6763-41a6-b899-b84f190c0d8b</uuid></powerup><powerup><x>1313.0</x><y>181.0</y><uuid>46c85199-552c-4564-bf03-80e9ef9dc4d7</uuid></powerup><powerup><x>1552.0</x><y>163.0</y><uuid>be61c8ac-15a6-4a34-9d50-5266da18ed27</uuid></powerup><powerup><x>1995.0</x><y>345.0</y><uuid>65b57410-6b07-4f24-a78a-a7c091b0aa86</uuid></powerup><powerup><x>2410.0</x><y>228.0</y><uuid>667c15ae-99d3-4915-9659-7b36601e6b51</uuid></powerup><powerup><x>2640.0</x><y>248.0</y><uuid>94d1b851-0e29-4f64-aa5a-494e91885a7d</uuid></powerup><powerup><x>2985.0</x><y>627.0</y><uuid>33acd47f-f396-4cdc-b7ba-b109f4dffd96</uuid></powerup><powerup><x>2404.0</x><y>846.0</y><uuid>6d573d42-9bff-433f-a424-96c970c87f6c</uuid></powerup><powerup><x>2186.0</x><y>720.0</y><uuid>fb18a69d-1ab8-47b1-9960-b57b400907bd</uuid></powerup><powerup><x>2015.0</x><y>592.0</y><uuid>c3967ee3-6ba4-4b7f-8629-6226df1f2264</uuid></powerup><powerup><x>1641.0</x><y>536.0</y><uuid>4679c590-5d98-4492-9733-fd96ca10c0ba</uuid></powerup><powerup><x>1418.0</x><y>579.0</y><uuid>25014576-3527-453e-b80e-38b2781fa65c</uuid></powerup><powerup><x>1211.0</x><y>663.0</y><uuid>514184fb-f219-4696-8e59-79ff0e371789</uuid></powerup><powerup><x>791.0</x><y>720.0</y><uuid>6be1ba6e-92c2-4605-a8e1-3eb7295e2e82</uuid></powerup><powerup><x>513.0</x><y>714.0</y><uuid>ff387e6e-e1f6-4dca-828a-eea50d958eca</uuid></powerup><powerup><x>330.0</x><y>604.0</y><uuid>8c2531b2-0b9c-414e-9fff-99e4fbc36209</uuid></powerup><powerup><x>233.0</x><y>393.0</y><uuid>676d992a-e41c-48ab-98c6-470c95a152db</uuid></powerup></root><root><missleOrigin><x>0.0</x><y>0.0</y><angle>0.0</angle></missleOrigin><earthOrigin><x>6354.0</x><y>1444.0</y></earthOrigin><isCameraFollowsMissle>true</isCameraFollowsMissle><fileName>Capella-648</fileName><creatorId>057dbe61</creatorId><uuid>7584d59a-f1b9-41ca-947b-d70031c1a9d7</uuid><isbuiltin>false</isbuiltin><wall><x>-143.0</x><y>-13.5</y><rotation>4.820079</rotation><width>210.07794</width><height>24.0</height><uuid>2b4a0352-0d5f-4b67-8a15-1c24d759169f</uuid></wall><wall><x>-72.5</x><y>-156.5</y><rotation>5.5876346</rotation><width>181.61345</width><height>24.0</height><uuid>fc231976-0b26-4dc6-82e2-e710183263be</uuid></wall><wall><x>76.0</x><y>-176.0</y><rotation>0.33869818</rotation><width>210.60118</width><height>24.0</height><uuid>564513a7-5ca1-40e8-a0aa-49a725a9c506</uuid></wall><wall><x>231.5</x><y>-75.0</y><rotation>0.8035779</rotation><width>218.4865</width><height>24.0</height><uuid>d7f3f383-521b-4be9-84b6-004408319445</uuid></wall><wall><x>332.0</x><y>108.5</y><rotation>1.2878482</rotation><width>260.4001</width><height>24.0</height><uuid>c536b422-a529-432f-aede-ee36f6f464cd</uuid></wall><wall><x>418.5</x><y>321.5</y><rotation>1.0774544</rotation><width>249.94247</width><height>24.0</height><uuid>6d20efee-457f-4995-bf03-e421f180585c</uuid></wall><wall><x>579.5</x><y>473.0</y><rotation>0.45053983</rotation><width>262.83258</width><height>24.0</height><uuid>e88d33b4-df99-46d1-83fb-40e41995e811</uuid></wall><wall><x>-153.0</x><y>79.0</y><rotation>1.5707964</rotation><width>24.0</width><height>24.0</height><uuid>df24255c-a876-4c82-9acb-b442aeeef5b6</uuid></wall><wall><x>-74.0</x><y>183.5</y><rotation>0.92347854</rotation><width>286.00192</width><height>24.0</height><uuid>2518e663-70b4-499e-bc4a-4b520ee0a5ae</uuid></wall><wall><x>-2.0</x><y>409.5</y><rotation>1.6283458</rotation><width>267.40295</width><height>24.0</height><uuid>8373b869-75c4-4d4c-accb-cb48946086f3</uuid></wall><wall><x>-9.0</x><y>531.0</y><rotation>1.5707964</rotation><width>24.0</width><height>24.0</height><uuid>05980ab2-a4a3-4a01-8d72-49b0dc35b268</uuid></wall><wall><x>609.0</x><y>762.5</y><rotation>0.35841587</rotation><width>1343.873</width><height>24.0</height><uuid>0cc026b0-5185-4ee8-81e7-78d70a87039e</uuid></wall><wall><x>687.0</x><y>525.0</y><rotation>1.5707964</rotation><width>24.0</width><height>24.0</height><uuid>8d9fdb6b-5ef0-43f5-9ba2-e65775543791</uuid></wall><wall><x>1055.0</x><y>663.5</y><rotation>0.35996127</rotation><width>810.4</width><height>24.0</height><uuid>2155bd22-8dbf-4677-8c8a-ffce7f11694d</uuid></wall><wall><x>1227.0</x><y>994.0</y><rotation>1.5707964</rotation><width>24.0</width><height>24.0</height><uuid>44f92dad-700a-4eea-8bfa-38cb742d7f9d</uuid></wall><wall><x>1270.0</x><y>1275.0</y><rotation>1.4189494</rotation><width>592.542</width><height>24.0</height><uuid>71efea24-af55-4b79-be5d-675a8cf0de0c</uuid></wall><wall><x>1423.0</x><y>802.0</y><rotation>1.5707964</rotation><width>24.0</width><height>24.0</height><uuid>348723e8-0abf-4079-92d4-95d791c55c8e</uuid></wall><wall><x>1496.0</x><y>1122.5</y><rotation>1.3468478</rotation><width>681.41693</width><height>24.0</height><uuid>10e29ae5-87f2-4c3c-b3f6-3f27af9fb6be</uuid></wall><wall><x>1313.0</x><y>1556.0</y><rotation>1.5707964</rotation><width>24.0</width><height>24.0</height><uuid>d8aeebdf-0c00-40c4-bd86-726af80f987e</uuid></wall><wall><x>1491.5</x><y>1784.0</y><rotation>0.9065728</rotation><width>603.1243</width><height>24.0</height><uuid>c8c2c324-5eaf-4ad7-8bf7-833e3772db21</uuid></wall><wall><x>1788.0</x><y>2071.5</y><rotation>0.46703163</rotation><width>288.30475</width><height>24.0</height><uuid>3d42d7d4-a0b6-4a22-9926-b0eefbcc1397</uuid></wall><wall><x>1993.0</x><y>2127.0</y><rotation>6.2372413</rotation><width>198.1838</width><height>24.0</height><uuid>eb1ead5c-8b59-4db4-8573-d3634eca471d</uuid></wall><wall><x>2138.0</x><y>2058.5</y><rotation>5.4447756</rotation><width>197.48486</width><height>24.0</height><uuid>86c87f9f-37a9-4384-a324-be1a63345e20</uuid></wall><wall><x>2320.5</x><y>1780.0</y><rotation>5.2393003</rotation><width>519.1616</width><height>24.0</height><uuid>4a967fba-4364-4bf0-94c3-8b34bee27762</uuid></wall><wall><x>1569.0</x><y>1443.0</y><rotation>1.5707964</rotation><width>24.0</width><height>24.0</height><uuid>05211247-193d-458f-bfb0-6bc1dea3d09d</uuid></wall><wall><x>1703.5</x><y>1624.0</y><rotation>0.9317298</rotation><width>475.00443</width><height>24.0</height><uuid>17a5b98d-3e10-43d8-af1a-f1ab9fb91ad0</uuid></wall><wall><x>1898.0</x><y>1835.5</y><rotation>0.4702919</rotation><width>158.61426</width><height>24.0</height><uuid>d4a0972e-1fbf-42cb-a3d4-1d7af10d468d</uuid></wall><wall><x>2011.0</x><y>1842.0</y><rotation>5.8579803</rotation><width>140.36151</width><height>24.0</height><uuid>c95a6c00-566c-4d71-a58a-1dbae4ec36b1</uuid></wall><wall><x>2192.5</x><y>1514.0</y><rotation>5.1123075</rotation><width>684.0856</width><height>24.0</height><uuid>75be9e65-c29f-41bf-8472-a75017e95577</uuid></wall><wall><x>4371.1445</x><y>1207.1044</y><rotation>0.0029701</rotation><width>4066.0178</width><height>24.0</height><uuid>5291c68f-4297-459e-8464-92d135272569</uuid></wall><wall><x>2445.0</x><y>1566.0</y><rotation>1.5707964</rotation><width>24.0</width><height>24.0</height><uuid>53d22248-6997-4386-8ecf-765a478b3d6f</uuid></wall><wall><x>4470.0</x><y>1621.5</y><rotation>0.027399048</rotation><width>4075.5208</width><height>24.0</height><uuid>77631766-c9cc-4616-8ee3-38a63a83ff47</uuid></wall><wall><x>6518.5</x><y>1393.5</y><rotation>4.795092</rotation><width>592.94464</width><height>24.0</height><uuid>4a36f835-224e-4364-8361-dc67a9215063</uuid></wall><wall><x>6415.0</x><y>1188.0</y><rotation>1.5707964</rotation><width>24.0</width><height>24.0</height><uuid>709598bd-96fd-44b1-9a41-f55792c9a674</uuid></wall><wall><x>6478.5</x><y>1149.0</y><rotation>5.7324095</rotation><width>173.04027</width><height>24.0</height><uuid>796fe095-a683-41f2-ac83-9feec354e719</uuid></wall><wall><x>6542.0</x><y>1110.0</y><rotation>1.5707964</rotation><width>24.0</width><height>24.0</height><uuid>70c868c6-a3cd-4fde-b4f3-92508e24d804</uuid></wall><wall><x>2332.5</x><y>2005.5</y><rotation>0.6281701</rotation><width>141.41379</width><height>24.0</height><uuid>47e82ccf-a6ae-49d0-8386-be9559e947c7</uuid></wall><wall><x>2404.5</x><y>2013.5</y><rotation>5.4585915</rotation><width>96.18033</width><height>24.0</height><uuid>c0836653-1fa6-45bb-ad50-8fe7484abd37</uuid></wall><wall><x>2413.0</x><y>1962.5</y><rotation>4.133865</rotation><width>82.5235</width><height>24.0</height><uuid>44c47dc1-2eb0-45f2-bc7a-4bb5ad74241d</uuid></wall><wall><x>2367.0</x><y>1951.0</y><rotation>2.7326849</rotation><width>89.39113</width><height>24.0</height><uuid>39920d7e-ee72-42e2-ba38-8212f517331e</uuid></wall><wall><x>2333.0</x><y>1919.5</y><rotation>4.622742</rotation><width>113.358826</width><height>24.0</height><uuid>bd585e49-3f01-4796-859e-2656d6fcd0d0</uuid></wall><wall><x>2376.0</x><y>1797.0</y><rotation>2.228685</rotation><width>79.60576</width><height>24.0</height><uuid>2a21ac26-abfa-4af7-a0ac-40b68830eebd</uuid></wall><wall><x>2397.0</x><y>1850.0</y><rotation>0.68429476</rotation><width>122.0816</width><height>24.0</height><uuid>e4e2aced-4715-495b-af71-c81ecdddaace</uuid></wall><wall><x>2454.5</x><y>1846.0</y><rotation>5.2207</rotation><width>104.13114</width><height>24.0</height><uuid>4b0a9b5b-51b6-47a2-9923-92539b1ddcea</uuid></wall><wall><x>2435.0</x><y>1881.0</y><rotation>1.5707964</rotation><width>24.0</width><height>24.0</height><uuid>4526754f-8000-4f8c-b33b-db709ed32e8d</uuid></wall><wall><x>2466.0</x><y>1900.5</y><rotation>0.56149375</rotation><width>97.24616</width><height>24.0</height><uuid>f6e030ef-75a8-43f0-a90f-953bbd105680</uuid></wall><wall><x>2510.5</x><y>1888.5</y><rotation>5.117281</rotation><width>92.54196</width><height>24.0</height><uuid>0fb2c436-a885-4d9d-98f8-d9cc8c1a6909</uuid></wall><wall><x>2493.0</x><y>1756.0</y><rotation>0.5404195</rotation><width>175.60475</width><height>24.0</height><uuid>7eecc7f3-1a9b-487c-befd-bad1ff9247d3</uuid></wall><wall><x>2520.5</x><y>1715.0</y><rotation>4.2740526</rotation><width>200.70596</width><height>24.0</height><uuid>c777b940-1b89-44d9-bafd-5b5a4695a0f8</uuid></wall><wall><x>2498.5</x><y>1721.0</y><rotation>5.4625077</rotation><width>84.141495</width><height>24.0</height><uuid>db8d9665-a686-40f9-ba05-55c235660621</uuid></wall><wall><x>2593.5</x><y>1688.5</y><rotation>1.1201351</rotation><width>196.19176</width><height>24.0</height><uuid>6ce69176-686d-4c3f-81de-e883eeac4f2c</uuid></wall><wall><x>2631.0</x><y>1766.0</y><rotation>1.5707964</rotation><width>24.0</width><height>24.0</height><uuid>e43fa574-c9a3-4b7a-9260-d376b16d95e4</uuid></wall><wall><x>2631.0</x><y>1766.0</y><rotation>1.5707964</rotation><width>24.0</width><height>24.0</height><uuid>bb5f21b4-b298-4569-be86-11d5317604ec</uuid></wall><wall><x>2660.5</x><y>1735.0</y><rotation>5.472999</rotation><width>109.58621</width><height>24.0</height><uuid>0fff093e-0cfc-4290-9c38-851f685cb7ed</uuid></wall><wall><x>2661.0</x><y>1665.5</y><rotation>4.0668135</rotation><width>120.40021</width><height>24.0</height><uuid>9dd335c0-acad-4b7c-b882-a1d78c27301a</uuid></wall><wall><x>2556.0</x><y>1611.0</y><rotation>1.5707964</rotation><width>24.0</width><height>24.0</height><uuid>f62739f1-e78a-4932-a677-ca9fbe99413d</uuid></wall><wall><x>2594.0</x><y>1619.0</y><rotation>0.20749655</rotation><width>101.665955</width><height>24.0</height><uuid>93e7b522-9ddd-4947-8a14-c14238c4399e</uuid></wall><wall><x>2632.0</x><y>1627.0</y><rotation>1.5707964</rotation><width>24.0</width><height>24.0</height><uuid>14272136-ebd7-474d-8b75-fd2120a946ed</uuid></wall><wall><x>2774.0</x><y>1687.0</y><rotation>4.712389</rotation><width>102.0</width><height>24.0</height><uuid>68a0d0bb-9c2a-4bb0-a657-06e59ecf3a70</uuid></wall><wall><x>2745.0</x><y>1622.0</y><rotation>3.8724992</rotation><width>101.89737</width><height>24.0</height><uuid>cde8e320-c9df-491e-9c0d-c03c8500b19c</uuid></wall><wall><x>2774.0</x><y>1648.0</y><rotation>1.5707964</rotation><width>24.0</width><height>24.0</height><uuid>18651aed-5c6c-4dc6-a6a4-70745e36a78d</uuid></wall><wall><x>2816.0</x><y>1670.5</y><rotation>0.49180916</rotation><width>119.29428</width><height>24.0</height><uuid>1fccf75a-2d53-4862-b42e-4e29f35f307c</uuid></wall><wall><x>2447.5</x><y>1281.0</y><rotation>1.6367383</rotation><width>130.23088</width><height>24.0</height><uuid>d37714f0-72f7-4f38-bbd4-8011d484f081</uuid></wall><wall><x>2491.0</x><y>1336.5</y><rotation>0.05314036</rotation><width>118.13288</width><height>24.0</height><uuid>819abd35-4089-4ef1-b96d-49117d0007ed</uuid></wall><wall><x>2478.5</x><y>1281.0</y><rotation>6.2542086</rotation><width>93.02898</width><height>24.0</height><uuid>0135a26d-5fa2-43a1-9d58-2a84ed3a6b62</uuid></wall><wall><x>2575.5</x><y>1249.5</y><rotation>4.6248937</rotation><width>81.21888</width><height>24.0</height><uuid>9c66e36f-9e40-4ef2-8d7e-8fd4a94b690f</uuid></wall><wall><x>2606.0</x><y>1221.0</y><rotation>0.0</rotation><width>90.0</width><height>24.0</height><uuid>755e21db-01da-4cec-b83f-b0a88237df22</uuid></wall><wall><x>2694.5</x><y>1256.5</y><rotation>4.60511</rotation><width>89.37584</width><height>24.0</height><uuid>7670c2e4-749f-4645-a8ab-67762b5e51f0</uuid></wall><wall><x>2736.0</x><y>1223.5</y><rotation>6.2720776</rotation><width>114.005554</width><height>24.0</height><uuid>92b086dd-138b-478b-8f7f-2282218320e1</uuid></wall><wall><x>2778.0</x><y>1258.5</y><rotation>1.6551031</rotation><width>95.25307</width><height>24.0</height><uuid>bffd47ab-40c1-47d5-9f61-f9aafea8ea2a</uuid></wall><wall><x>2736.5</x><y>1291.5</y><rotation>3.2064369</rotation><width>101.16217</width><height>24.0</height><uuid>f7c4a8cd-e46e-40f8-a197-7d7079bf800e</uuid></wall><wall><x>2698.0</x><y>1289.0</y><rotation>1.5707964</rotation><width>24.0</width><height>24.0</height><uuid>36be5e90-7404-4e24-91ec-167b233b23f9</uuid></wall><wall><x>2698.0</x><y>1289.0</y><rotation>1.5707964</rotation><width>24.0</width><height>24.0</height><uuid>bb11887c-8d80-4873-8991-e8cd39963441</uuid></wall><wall><x>2842.0</x><y>1265.5</y><rotation>4.6174374</rotation><width>87.285065</width><height>24.0</height><uuid>6032d7e8-2cd5-43a8-8d86-a4154016ac38</uuid></wall><wall><x>2878.5</x><y>1228.0</y><rotation>6.132439</rotation><width>103.9062</width><height>24.0</height><uuid>b2bd431e-9216-4049-8011-4a0fcb4ad32a</uuid></wall><wall><x>2919.5</x><y>1266.5</y><rotation>1.5371013</rotation><width>113.050545</width><height>24.0</height><uuid>e24039bd-2298-4ce3-a28c-4d8c7c9e052b</uuid></wall><wall><x>2883.0</x><y>1304.0</y><rotation>3.323761</rotation><width>101.27872</width><height>24.0</height><uuid>d8cdaceb-1469-4a18-b20c-dea4bd45d331</uuid></wall><wall><x>2845.0</x><y>1297.0</y><rotation>1.5707964</rotation><width>24.0</width><height>24.0</height><uuid>5580f75c-be8a-4f8b-aabe-7ca124f1812f</uuid></wall><wall><x>2996.5</x><y>1286.0</y><rotation>1.5208379</rotation><width>124.12492</width><height>24.0</height><uuid>93b820ef-31e5-49fe-a3fc-3f9deb11c648</uuid></wall><wall><x>3037.5</x><y>1310.0</y><rotation>0.7754975</rotation><width>95.42129</width><height>24.0</height><uuid>15fc4495-f6b0-4757-b130-1222bafa28fa</uuid></wall><wall><x>3076.5</x><y>1305.0</y><rotation>5.135243</rotation><width>89.795135</width><height>24.0</height><uuid>5778fcc9-724a-4bc2-b6e2-f1d2e7d35709</uuid></wall><wall><x>3211.5</x><y>1256.0</y><rotation>4.6344223</rotation><width>88.195015</width><height>24.0</height><uuid>d7651b3f-8672-4a62-958d-d9c261bedfda</uuid></wall><wall><x>3302.0</x><y>1305.0</y><rotation>4.5917654</rotation><width>156.96616</width><height>24.0</height><uuid>c1b7a6e5-5eb4-4e65-9a4f-62a1ee5c48e1</uuid></wall><wall><x>3309.0</x><y>1297.0</y><rotation>0.09966857</rotation><width>104.399</width><height>24.0</height><uuid>b168b443-0444-4627-a6ac-2e301b1b34c8</uuid></wall><powerup><x>290.0</x><y>418.0</y><uuid>e48a6819-afaf-460e-8f1f-c63f8403c364</uuid></powerup><powerup><x>508.0</x><y>658.0</y><uuid>10f2e617-7fb2-453b-aac0-2ae14a8cbf67</uuid></powerup><powerup><x>952.0</x><y>689.0</y><uuid>2351fa07-8315-42ba-8f5b-5c085514ba6d</uuid></powerup><powerup><x>1635.0</x><y>1797.0</y><uuid>6c71d8af-2e8e-4db8-b8fb-d73fbaf14032</uuid></powerup><powerup><x>1889.0</x><y>2059.0</y><uuid>724f2fdb-cc15-4846-b43c-4db2ef5eb24e</uuid></powerup><powerup><x>2210.0</x><y>1689.0</y><uuid>f191ce25-ae06-4a28-8618-2ad8f2cf769b</uuid></powerup></root><root><missleOrigin><x>0.0</x><y>0.0</y><angle>0.0</angle></missleOrigin><earthOrigin><x>261.87778</x><y>1154.3159</y></earthOrigin><isCameraFollowsMissle>true</isCameraFollowsMissle><fileName>Mebsuta-432</fileName><creatorId>057dbe61</creatorId><uuid>72c61955-f4e1-4aa2-af48-a1300648ed4d</uuid><isbuiltin>false</isbuiltin><wall><x>289.0</x><y>70.0</y><rotation>0.68469465</rotation><width>407.94608</width><height>24.0</height><uuid>6acd7f40-01b8-4ba2-b11c-6366741e0445</uuid></wall><wall><x>3.0</x><y>-76.0</y><rotation>-3.020969</rotation><width>265.9323</width><height>24.0</height><uuid>3f59472d-4883-4876-b424-adaf6bdc8d50</uuid></wall><wall><x>-131.0</x><y>19.0</y><rotation>1.6207547</rotation><width>220.27483</width><height>24.0</height><uuid>fa585e09-86e6-402c-8314-99d21dff0b43</uuid></wall><wall><x>-23.0</x><y>159.0</y><rotation>0.26550967</rotation><width>236.2795</width><height>24.0</height><uuid>7726d708-a8af-4adb-9afa-1cfeb24a439b</uuid></wall><wall><x>176.0</x><y>253.0</y><rotation>0.59926623</rotation><width>221.61679</width><height>24.0</height><uuid>c6af8006-9b41-418f-9516-48ad909bff09</uuid></wall><wall><x>357.0</x><y>250.0</y><rotation>-0.5349551</rotation><width>219.69296</width><height>24.0</height><uuid>1a3753d0-0c56-4af1-ac8a-6c5f355efb09</uuid></wall><wall><x>490.0</x><y>354.0</y><rotation>-0.57907426</rotation><width>217.45114</width><height>24.0</height><uuid>76d1c234-5720-45b3-8f9f-9e64c9a5aa6b</uuid></wall><wall><x>493.0</x><y>493.0</y><rotation>0.7397157</rotation><width>247.74583</width><height>24.0</height><uuid>4e68835c-d354-4c0b-ae14-556debc39613</uuid></wall><wall><x>627.5714</x><y>703.7229</y><rotation>1.2342908</rotation><width>278.62698</width><height>24.0</height><uuid>318f92fe-d780-48be-9deb-f13399e79290</uuid></wall><wall><x>851.4574</x><y>655.9744</y><rotation>1.3006431</rotation><width>337.23138</width><height>24.0</height><uuid>475f2c6d-f990-470e-a625-c77e954e7b75</uuid></wall><wall><x>753.6428</x><y>996.82684</y><rotation>1.1096057</rotation><width>364.03296</width><height>24.0</height><uuid>c99de794-0002-45b8-9c7a-c257d2bb7404</uuid></wall><wall><x>967.5714</x><y>950.6563</y><rotation>1.0810299</rotation><width>308.2353</width><height>24.0</height><uuid>4e195560-b9af-4da3-8909-2757821263bd</uuid></wall><wall><x>952.27325</x><y>1316.4949</y><rotation>0.9222191</rotation><width>394.00507</width><height>24.0</height><uuid>85ac25f8-e9af-4165-9445-6c97677a8ac1</uuid></wall><wall><x>1163.648</x><y>1245.4546</y><rotation>0.9080193</rotation><width>404.67517</width><height>24.0</height><uuid>c9167fa8-c6b6-4ec5-98ff-258bbd3471ac</uuid></wall><wall><x>1494.0</x><y>1401.0</y><rotation>0.022552567</rotation><width>399.1015</width><height>24.0</height><uuid>39a00bb5-faff-40d4-ab23-dfa2911ca16e</uuid></wall><wall><x>1709.0</x><y>1535.0</y><rotation>1.5563569</rotation><width>277.02887</width><height>24.0</height><uuid>5aa45e1d-6f43-408e-bf6c-d728ab512ed4</uuid></wall><wall><x>1282.0</x><y>1712.0</y><rotation>3.0453699</rotation><width>895.14075</width><height>24.0</height><uuid>cfce2e7b-b08a-436c-b59b-1004eb61d4ec</uuid></wall><wall><x>833.0</x><y>1631.0</y><rotation>-1.5971061</rotation><width>266.0921</width><height>24.0</height><uuid>473903c1-a1b2-4fba-bcb8-739bdac818bf</uuid></wall><wall><x>940.0</x><y>1481.0</y><rotation>-0.1658105</rotation><width>248.40692</width><height>24.0</height><uuid>abc6d5bc-3622-4c62-804f-58a46449725e</uuid></wall><wall><x>698.0</x><y>1625.0</y><rotation>-1.5631919</rotation><width>263.0076</width><height>24.0</height><uuid>586b8043-74ef-431a-bcf1-c2bb11bd9d62</uuid></wall><wall><x>545.0</x><y>1758.0</y><rotation>3.1061046</rotation><width>338.21295</width><height>24.0</height><uuid>1fee6ee7-b3f2-4b80-98d0-2025b35e33a9</uuid></wall><wall><x>551.0</x><y>1510.0</y><rotation>3.0616353</rotation><width>313.0</width><height>24.0</height><uuid>1ff79898-f280-460c-8942-c1eca8556eee</uuid></wall><wall><x>274.0</x><y>1731.0</y><rotation>-2.8118637</rotation><width>237.81085</width><height>24.0</height><uuid>1cc689b1-405b-4402-b613-51c3cb0f5bb4</uuid></wall><wall><x>295.0</x><y>1467.0</y><rotation>0.42303392</rotation><width>253.3318</width><height>24.0</height><uuid>da6eb437-3a00-4d7a-9313-b2b58baf8b57</uuid></wall><wall><x>20.471088</x><y>1552.6995</y><rotation>-2.3579216</rotation><width>409.41544</width><height>24.0</height><uuid>c7e5bcb9-f545-4b34-ade6-65dc97311c38</uuid></wall><wall><x>120.98134</x><y>1316.7303</y><rotation>-2.1488435</rotation><width>250.73691</width><height>24.0</height><uuid>fb3ae9c9-2d64-4fc8-9c7b-315979537e4d</uuid></wall><wall><x>-180.71263</x><y>1261.772</y><rotation>-1.9446324</rotation><width>334.07333</width><height>24.0</height><uuid>517bc8b5-b310-4ebf-9eef-b85c995a0b12</uuid></wall><wall><x>26.71791</x><y>1103.1653</y><rotation>-1.81782</rotation><width>237.20033</width><height>24.0</height><uuid>8b0a8eed-614b-4bf5-8787-779ab72d2187</uuid></wall><wall><x>52.5226</x><y>877.3612</y><rotation>-1.1493771</rotation><width>254.24397</width><height>24.0</height><uuid>8628f9ab-ba48-41b2-ae68-5aa7ca923efa</uuid></wall><wall><x>-178.87378</x><y>871.2609</y><rotation>-1.3239056</rotation><width>507.38547</width><height>24.0</height><uuid>b4588361-cc95-4f1a-959d-69af13b9db1d</uuid></wall><wall><x>21.46103</x><y>503.19043</y><rotation>-0.7411466</rotation><width>383.6274</width><height>24.0</height><uuid>18d899d6-a85e-4359-8d71-32076f94d3cd</uuid></wall><wall><x>236.63441</x><y>639.3369</y><rotation>-0.7552186</rotation><width>374.93732</width><height>24.0</height><uuid>1d115290-d63c-44a8-b153-34e6b614d74b</uuid></wall><wall><x>251.28738</x><y>443.5519</y><rotation>0.61072594</rotation><width>256.33768</width><height>24.0</height><uuid>987f0122-5b33-487c-aa05-8e295f1a6806</uuid></wall><wall><x>575.0</x><y>183.0</y><rotation>0.7092927</rotation><width>325.5042</width><height>24.0</height><uuid>056d7e5a-d26b-4993-814c-fb43e753a7c0</uuid></wall><wall><x>592.35693</x><y>-10.802109</y><rotation>-0.63097703</rotation><width>303.4238</width><height>24.0</height><uuid>e630d27e-e4bd-4c62-9a4c-ed296a842906</uuid></wall><wall><x>875.9018</x><y>129.08896</y><rotation>-0.6930688</rotation><width>460.1652</width><height>24.0</height><uuid>102aa2fa-756f-4e06-a11d-24107a1e5657</uuid></wall><wall><x>878.20795</x><y>-224.58954</y><rotation>-0.65658176</rotation><width>412.83533</width><height>24.0</height><uuid>0f3de660-0245-4986-bb56-4d4262e8caa1</uuid></wall><wall><x>1115.9629</x><y>-79.947815</y><rotation>-0.7575169</rotation><width>177.55281</width><height>24.0</height><uuid>7eef836a-7648-4e21-bb07-d10d4f6dc676</uuid></wall><wall><x>1143.6979</x><y>-376.724</y><rotation>-0.24941322</rotation><width>218.76929</width><height>24.0</height><uuid>a9926f18-5ccf-43e3-988a-c74d963389ef</uuid></wall><wall><x>1325.8403</x><y>-379.61398</y><rotation>0.30731112</rotation><width>175.20845</width><height>24.0</height><uuid>bce6af38-6ada-4642-a0d8-7aecd7382727</uuid></wall><wall><x>1488.5953</x><y>-273.13672</y><rotation>0.77926326</rotation><width>230.52115</width><height>24.0</height><uuid>f6b17a51-117e-48d5-b9e0-0d34939563c3</uuid></wall><wall><x>1573.7993</x><y>-109.538895</y><rotation>1.5027199</rotation><width>176.40862</width><height>24.0</height><uuid>43c790cf-b5e2-4f4e-91ab-982679f41ccd</uuid></wall><wall><x>1225.1465</x><y>-135.30392</y><rotation>0.14981246</rotation><width>107.200745</width><height>24.0</height><uuid>074eec1f-eb44-4c5e-b2be-302c4d55228c</uuid></wall><wall><x>1287.5952</x><y>119.021835</y><rotation>1.5805236</rotation><width>514.0243</width><height>24.0</height><uuid>566e442a-e175-4f2c-b507-565e8168fb1c</uuid></wall><wall><x>1550.9879</x><y>272.36017</y><rotation>1.6733025</rotation><width>596.12915</width><height>24.0</height><uuid>8d475f86-0ba6-4660-a891-42dc55f513a3</uuid></wall><wall><x>1243.0</x><y>475.0</y><rotation>1.9756881</rotation><width>236.08896</width><height>24.0</height><uuid>0d38ee18-7fc9-4d69-8707-1a558226bf86</uuid></wall><wall><x>1457.554</x><y>693.9662</y><rotation>2.0450335</rotation><width>295.62476</width><height>24.0</height><uuid>49bae95d-a716-4538-b546-ae98ae83c7bb</uuid></wall><wall><x>1080.0</x><y>641.0</y><rotation>2.6551893</rotation><width>314.47256</width><height>24.0</height><uuid>01a1417c-addd-4db6-9dda-f0063f24c646</uuid></wall><wall><x>1239.0</x><y>874.0</y><rotation>2.8316705</rotation><width>347.55862</width><height>24.0</height><uuid>74f99d3c-e2df-4cc4-9f9d-061788b93444</uuid></wall><wall><x>1018.0</x><y>818.0</y><rotation>0.99407995</rotation><width>245.74783</width><height>24.0</height><uuid>402540bd-5358-4c6b-8900-38bb7e968136</uuid></wall><wall><x>603.0</x><y>307.0</y><rotation>0.6998929</rotation><width>74.518456</width><height>24.0</height><uuid>6959872a-a433-4cef-a45b-85af43abb863</uuid></wall><wall><x>749.0</x><y>321.0</y><rotation>-0.042527534</rotation><width>235.21268</width><height>24.0</height><uuid>ed3e6290-9af1-46a7-be61-25d14267a6b5</uuid></wall><wall><x>836.0</x><y>409.0</y><rotation>-1.3078016</rotation><width>188.48077</width><height>24.0</height><uuid>c7c1cfdb-9a5c-4307-89ca-68626c9054b1</uuid></wall><wall><x>337.0</x><y>732.0</y><rotation>2.4756234</rotation><width>380.08987</width><height>24.0</height><uuid>ce4f68c7-1f3d-44d0-9142-d8fa43b6f46c</uuid></wall><wall><x>158.0</x><y>934.0</y><rotation>1.971746</rotation><width>223.84995</width><height>24.0</height><uuid>aee1607e-2912-4eb1-a799-eebec11b0158</uuid></wall><wall><x>129.0</x><y>1113.5</y><rotation>1.4570043</rotation><width>200.13914</width><height>24.0</height><uuid>13a9ee28-b80a-4fdd-a243-96019b89bdba</uuid></wall><wall><x>189.5</x><y>1265.0</y><rotation>0.9027602</rotation><width>187.04907</width><height>24.0</height><uuid>1949283e-b047-499d-bead-28e20af5839d</uuid></wall><wall><x>346.0</x><y>1375.5</y><rotation>0.4133998</rotation><width>255.50162</width><height>24.0</height><uuid>a0113324-77d0-4292-b5e1-9a7d8e75586d</uuid></wall><wall><x>665.5</x><y>1407.0</y><rotation>6.2130427</rotation><width>452.05258</width><height>24.0</height><uuid>199ecb02-f379-4f3f-a664-a7bec48138e1</uuid></wall><wall><x>678.0</x><y>1007.0</y><rotation>4.2312355</rotation><width>892.6219</width><height>24.0</height><uuid>cab968e1-0988-43e3-9d53-c33deaf01a63</uuid></wall><wall><x>477.0</x><y>622.0</y><rotation>1.5707964</rotation><width>24.0</width><height>24.0</height><uuid>bb121936-31ad-4895-9182-1c625a80bf66</uuid></wall><powerup><x>728.0</x><y>617.0</y><uuid>37d9de03-c800-4696-8480-ffe5e78cac6f</uuid></powerup><powerup><x>790.0</x><y>855.0</y><uuid>66eda69e-04a0-4f1d-8a4f-96f468c672c9</uuid></powerup><powerup><x>980.0</x><y>1188.0</y><uuid>24526f87-7e5f-47b5-a58a-6b232c5f9a8b</uuid></powerup><powerup><x>1235.0</x><y>1510.0</y><uuid>985d1eaa-c5fe-4dbe-b1aa-31a18c7ae460</uuid></powerup><powerup><x>1461.0</x><y>1544.0</y><uuid>eddfc3e9-40fd-4621-9de8-9e419bb7fb3e</uuid></powerup><powerup><x>207.0</x><y>1583.0</y><uuid>d94d7b1e-9bff-4a0f-b138-bb2b9a6ecc4b</uuid></powerup><powerup><x>-36.0</x><y>1365.0</y><uuid>0c267e8c-8616-4bbe-81eb-0fd60447d1a7</uuid></powerup><powerup><x>-116.0</x><y>1005.0</y><uuid>6ea5fa80-cdfa-4043-8de8-056a56048329</uuid></powerup><powerup><x>-1.0</x><y>722.0</y><uuid>55b6d8c3-88c0-40d0-a70e-31a3ccffb8cb</uuid></powerup><powerup><x>887.0</x><y>-59.0</y><uuid>8f1fd5c4-1918-4d6f-99af-76a0ba08250d</uuid></powerup><powerup><x>1120.0</x><y>-259.0</y><uuid>40f0a897-0c09-4814-b2a3-66b5b8012a89</uuid></powerup><powerup><x>1436.0</x><y>-145.0</y><uuid>424d890b-6d7e-43dc-adf7-0697a21894e5</uuid></powerup><powerup><x>1419.0</x><y>367.0</y><uuid>baa9a1a7-4750-4238-80da-02f32ff4d13c</uuid></powerup><powerup><x>1357.0</x><y>630.0</y><uuid>bec45920-c6f4-4b67-b4e5-19d7f93d9be2</uuid></powerup><warp><x>291.0</x><y>202.0</y><rotation>0.0</rotation><name>WARP-058aef</name><connectsTo>WARP-4e7de4</connectsTo><uuid>9058aef7-bc2f-4161-8bb9-03ff058ac837</uuid></warp><warp><x>553.0</x><y>401.0</y><rotation>0.0</rotation><name>WARP-4e7de4</name><connectsTo>WARP-4e7de4</connectsTo><uuid>84e7de43-102d-4d7d-80ea-993f1177637b</uuid></warp><warp><x>1621.0</x><y>1546.0</y><rotation>0.0</rotation><name>WARP-234dd8</name><connectsTo>WARP-4e7de4</connectsTo><uuid>2234dd8d-f751-4739-aa18-1b289ff150c3</uuid></warp><warp><x>190.0</x><y>519.0</y><rotation>0.0</rotation><name>WARP-3e3409</name><connectsTo>WARP-047563</connectsTo><uuid>e3e34094-a5e0-4279-aba6-a2ae3485b3b6</uuid></warp><warp><x>923.0</x><y>1614.0</y><rotation>0.0</rotation><name>WARP-b55bac</name><connectsTo>WARP-0e30e1</connectsTo><uuid>7b55bacc-8398-4d59-bb65-2d4ffa87d6df</uuid></warp><warp><x>607.45605</x><y>1614.8501</y><rotation>0.0</rotation><name>WARP-0e30e1</name><connectsTo>WARP-0e30e1</connectsTo><uuid>50e30e12-7151-4715-a5ab-5da98873d06d</uuid></warp><warp><x>1135.0</x><y>759.0</y><rotation>0.0</rotation><name>WARP-c6b300</name><connectsTo>WARP-7f3bb5</connectsTo><uuid>3c6b3005-576d-411b-b553-8ec478c52e55</uuid></warp><warp><x>644.0</x><y>110.0</y><rotation>0.0</rotation><name>WARP-047563</name><connectsTo>WARP-047563</connectsTo><uuid>d047563c-9fd9-454f-8427-58fe7c0cee29</uuid></warp><warp><x>524.37695</x><y>1053.8138</y><rotation>0.0</rotation><name>WARP-7f3bb5</name><connectsTo>WARP-7f3bb5</connectsTo><uuid>87f3bb50-1c60-4f0a-ba22-59dd0277f76d</uuid></warp></root><root><missleOrigin><x>0.0</x><y>0.0</y><angle>0.0</angle></missleOrigin><earthOrigin><x>4505.0</x><y>-234.0</y></earthOrigin><isCameraFollowsMissle>true</isCameraFollowsMissle><fileName>Zuben-424</fileName><creatorId>057dbe61</creatorId><uuid>80efb5c2-1610-414e-a5b8-85ab4f912aa0</uuid><isbuiltin>false</isbuiltin><wall><x>-87.0</x><y>31.0</y><rotation>-1.6298186</rotation><width>220.38376</width><height>16.0</height><uuid>47630b9f-976f-4fcc-81d9-0897bfde0104</uuid></wall><wall><x>19.0</x><y>-88.0</y><rotation>-0.072453596</rotation><width>248.65237</width><height>16.0</height><uuid>8acc18c8-75cc-478d-a229-ba20f124bdcc</uuid></wall><wall><x>247.0</x><y>154.0</y><rotation>0.071307465</rotation><width>659.67645</width><height>16.0</height><uuid>41db7e7a-080d-43f5-a0ee-03f38b4e68be</uuid></wall><wall><x>355.0</x><y>-37.0</y><rotation>0.2720533</rotation><width>483.79333</width><height>16.0</height><uuid>afcfeb08-0329-41e7-bc6d-66140346f816</uuid></wall><wall><x>833.0</x><y>254.0</y><rotation>0.27561215</rotation><width>562.2188</width><height>16.0</height><uuid>8d3fda0c-03af-4c5d-a378-938fd7698505</uuid></wall><wall><x>915.0</x><y>16.0</y><rotation>-0.016741207</rotation><width>657.0921</width><height>16.0</height><uuid>dd4bff9c-c4c4-4758-9a70-b9af73e2aab5</uuid></wall><wall><x>1467.0</x><y>319.0</y><rotation>-0.011283019</rotation><width>709.0451</width><height>16.0</height><uuid>49b0e622-f9a6-4dc0-af6c-e6c987e5b51e</uuid></wall><wall><x>1526.0</x><y>9.0</y><rotation>0.022644212</rotation><width>574.1472</width><height>16.0</height><uuid>c7724847-135b-4650-bd60-1e55e3255960</uuid></wall><wall><x>2203.31</x><y>22.520912</y><rotation>-0.64919096</rotation><width>984.21594</width><height>24.0</height><uuid>01f69204-67fa-45b0-afc1-259318c416f2</uuid></wall><wall><x>2017.6396</x><y>-143.39407</y><rotation>-0.64500886</rotation><width>530.6006</width><height>24.0</height><uuid>10b2e835-02df-43ab-b44a-6d6c9e885e36</uuid></wall><wall><x>2246.0454</x><y>-537.57806</y><rotation>-1.4879985</rotation><width>483.6569</width><height>24.0</height><uuid>99026185-2e8e-4316-9a74-ea15048ea2b8</uuid></wall><wall><x>2630.755</x><y>-520.1094</y><rotation>-1.418894</rotation><width>508.8595</width><height>24.0</height><uuid>bd91b195-cea0-49c2-9b05-3954dbe5f538</uuid></wall><wall><x>2279.6604</x><y>-887.3453</y><rotation>-1.4295416</rotation><width>220.1931</width><height>24.0</height><uuid>f5ffd139-4be5-43c5-931d-9b9291cf8e8e</uuid></wall><wall><x>2391.2656</x><y>-1008.6567</y><rotation>-0.018516403</rotation><width>216.03703</width><height>24.0</height><uuid>4747ec80-9689-4b4b-86b7-253e991fb6ed</uuid></wall><wall><x>2915.361</x><y>-1062.0337</y><rotation>-0.87003815</rotation><width>744.4199</width><height>24.0</height><uuid>356b6280-933d-4df3-9a3a-c5efa5669bc3</uuid></wall><wall><x>2685.786</x><y>-1240.6041</y><rotation>-0.87223804</rotation><width>603.3142</width><height>24.0</height><uuid>68f66c2b-0f03-4e3c-a445-18cbfd394171</uuid></wall><wall><x>3213.393</x><y>-1351.603</y><rotation>-0.15204327</rotation><width>125.4472</width><height>24.0</height><uuid>d1cb5343-33ba-4e2a-ae3d-bf8b4cff5c61</uuid></wall><wall><x>3145.7925</x><y>-1572.3591</y><rotation>-0.36829227</rotation><width>577.7413</width><height>24.0</height><uuid>1063a404-ec74-4cb9-a447-be71e9fed735</uuid></wall><wall><x>3427.4827</x><y>-1166.2086</y><rotation>0.89421034</rotation><width>507.87793</width><height>24.0</height><uuid>f9d281bd-849b-432d-9908-a899b9f6c832</uuid></wall><wall><x>3661.8633</x><y>-1319.061</y><rotation>0.9434881</rotation><width>889.3166</width><height>24.0</height><uuid>0418ea1c-6679-47ef-9cb5-c05cbfe6514c</uuid></wall><wall><x>3489.344</x><y>-693.42883</y><rotation>1.8805621</rotation><width>606.88464</width><height>24.0</height><uuid>bdd2ff23-a055-4337-80e2-42b6595ea1f1</uuid></wall><wall><x>4048.5154</x><y>-665.14606</y><rotation>1.1830667</rotation><width>642.7083</width><height>24.0</height><uuid>3a5bf030-4ec2-4362-9389-830e85244956</uuid></wall><wall><x>3587.0</x><y>-386.0</y><rotation>0.1460907</rotation><width>370.95148</width><height>24.0</height><uuid>f5f3bc04-4fa9-4a6c-96a8-416f09d7781c</uuid></wall><wall><x>3772.476</x><y>-540.03503</y><rotation>-1.4969662</rotation><width>338.9233</width><height>24.0</height><uuid>3ff036a4-8eec-445a-a6a7-d7282d3745dd</uuid></wall><wall><x>3904.1064</x><y>-435.222</y><rotation>1.1506659</rotation><width>585.95734</width><height>24.0</height><uuid>0ab727b9-bc08-42a3-8a5c-bc78d8fc1d26</uuid></wall><wall><x>4342.0</x><y>-465.0</y><rotation>-0.4871727</rotation><width>380.23676</width><height>24.0</height><uuid>e2e91a13-f90d-4032-adb7-148eb29b5300</uuid></wall><wall><x>4606.174</x><y>-368.31372</y><rotation>1.1355387</rotation><width>441.13037</width><height>24.0</height><uuid>8d4a634e-7b81-4267-98f4-875c820f2a6d</uuid></wall><wall><x>4230.2114</x><y>-115.05904</y><rotation>0.2914568</rotation><width>438.49286</width><height>24.0</height><uuid>8543c028-5195-46d2-b36d-bfccef009781</uuid></wall><wall><x>4558.779</x><y>-111.77037</y><rotation>-0.44536555</rotation><width>318.022</width><height>24.0</height><uuid>1281b1fb-d508-4421-92bd-51200ec66978</uuid></wall><powerup><x>318.0</x><y>62.0</y><uuid>885ae6f4-1f6c-4795-83de-5f0f6453a07d</uuid></powerup><powerup><x>563.0</x><y>95.0</y><uuid>321a0533-1010-4858-9918-2798c801ea81</uuid></powerup><powerup><x>888.0</x><y>139.0</y><uuid>e2f8ec53-c2db-4711-968f-c5d2debf5848</uuid></powerup><powerup><x>1211.0</x><y>115.0</y><uuid>f38d6580-d55b-4e22-9201-8f1e4d07b614</uuid></powerup><powerup><x>1508.0</x><y>130.0</y><uuid>833f5ec4-164f-4084-92db-c3ae69c31c3c</uuid></powerup><powerup><x>1993.0</x><y>96.0</y><uuid>f725c1a9-ec36-4de2-822f-7976546c515c</uuid></powerup><powerup><x>2325.0</x><y>-233.0</y><uuid>43eb62ae-431f-4892-9592-3ef204d67f71</uuid></powerup><powerup><x>2591.0</x><y>-670.0</y><uuid>60adb961-7caa-4040-a3a4-57b09203615a</uuid></powerup><powerup><x>2764.0</x><y>-1109.0</y><uuid>eedcb242-7eb9-4732-bcf1-adf55095099c</uuid></powerup><powerup><x>2875.0</x><y>-1279.0</y><uuid>b5d1d93d-71dc-44af-af7b-179bf4dddd50</uuid></powerup><powerup><x>3023.0</x><y>-1413.0</y><uuid>bc9c8483-61a2-4e0e-b808-ba6581cb6701</uuid></powerup><powerup><x>3253.0</x><y>-1482.0</y><uuid>20b4e90e-5d3a-44d1-a388-abb6cf79aa83</uuid></powerup><powerup><x>3713.0</x><y>-833.0</y><uuid>9fda13a8-a426-48c0-9876-2d15a12e4eca</uuid></powerup><powerup><x>3923.0</x><y>-697.0</y><uuid>d3b0ed5b-8d80-4b71-b4f5-43009542504f</uuid></powerup><powerup><x>4008.0</x><y>-500.0</y><uuid>6da0c247-89b4-4817-82db-5e5d956216e2</uuid></powerup><powerup><x>4093.0</x><y>-260.0</y><uuid>5af399b8-a6bd-4426-9821-e971b5faa801</uuid></powerup></root><root><missleOrigin><x>220.12918</x><y>28.485615</y><angle>-5.5606203</angle></missleOrigin><earthOrigin><x>2213.25</x><y>-286.29788</y></earthOrigin><isCameraFollowsMissle>true</isCameraFollowsMissle><fileName>Megrez-410</fileName><creatorId>057dbe61</creatorId><uuid>2f7b7bd1-9fab-411d-913e-fa55ac7f0780</uuid><isbuiltin>false</isbuiltin><wall><x>376.37988</x><y>-7.4291477</y><rotation>0.6816379</rotation><width>406.133</width><height>16.0</height><uuid>2f7b7bd1-9fab-411d-913e-fa55ac7f0780</uuid></wall><wall><x>295.12903</x><y>235.93192</y><rotation>0.65330315</rotation><width>572.3862</width><height>16.0</height><uuid>5a84bb84-1b89-4811-8556-424ff5c84e2b</uuid></wall><wall><x>962.3157</x><y>285.39993</y><rotation>-0.9839148</rotation><width>494.79288</width><height>16.0</height><uuid>d26c3f07-58e4-4aa6-b0c8-5ccccee2745d</uuid></wall><wall><x>995.875</x><y>-182.02127</y><rotation>-1.4851693</rotation><width>233.8568</width><height>16.0</height><uuid>252bc48e-f4ab-4c7f-9867-77b24b0b81cb</uuid></wall><wall><x>1433.0</x><y>-194.25531</y><rotation>-0.01738955</rotation><width>115.01739</width><height>16.0</height><uuid>0979e0e9-7c03-4c24-9c75-e384eb97b65b</uuid></wall><wall><x>2030.375</x><y>-235.1915</y><rotation>-0.012421722</rotation><width>483.03726</width><height>16.0</height><uuid>b4617832-8b69-48cb-83ea-8f7f9b91d961</uuid></wall><wall><x>1328.0</x><y>-524.0</y><rotation>0.55411726</rotation><width>212.84972</width><height>16.0</height><uuid>86737d19-9fe6-4c3d-96e3-ae630c117ff7</uuid></wall><wall><x>1601.0</x><y>-335.0</y><rotation>1.7540818</rotation><width>208.4922</width><height>16.0</height><uuid>0160683c-4938-4951-a429-507e7a36cfa8</uuid></wall><wall><x>1682.1295</x><y>-229.51447</y><rotation>-0.023037398</rotation><width>217.0576</width><height>16.0</height><uuid>9a45db65-d904-48e0-9e5b-a2298f1e73a0</uuid></wall><wall><x>1526.0</x><y>-318.0</y><rotation>-1.3219641</rotation><width>251.75385</width><height>16.0</height><uuid>ff038f86-fcd0-426b-b615-a70f245c6a21</uuid></wall><wall><x>1582.3799</x><y>-440.81207</y><rotation>0.015623729</rotation><width>64.00781</width><height>16.0</height><uuid>680d78c0-67ed-4ac8-a495-1f22bb73d503</uuid></wall><wall><x>614.0021</x><y>-20.407852</y><rotation>-1.0010399</rotation><width>304.0263</width><height>16.0</height><uuid>8f0cd5e4-d977-4e5b-b023-9848229f54f2</uuid></wall><wall><x>847.1272</x><y>-356.76974</y><rotation>-0.9439992</rotation><width>514.8718</width><height>16.0</height><uuid>e73815ec-47d7-46ed-b01e-529066c0fa13</uuid></wall><wall><x>1115.3787</x><y>-570.0885</y><rotation>-0.100467056</rotation><width>249.2569</width><height>16.0</height><uuid>db15ca91-45b5-490d-918f-48e8d666678c</uuid></wall><wall><x>911.0</x><y>-75.0</y><rotation>0.020545054</rotation><width>146.03082</width><height>16.0</height><uuid>950cac3c-44c4-4247-b85e-2d87c0215ee2</uuid></wall><wall><x>925.0</x><y>-184.0</y><rotation>-0.93704003</rotation><width>266.81268</width><height>16.0</height><uuid>2bf467c6-5fd7-49f8-af83-13739f44e659</uuid></wall><wall><x>2022.0</x><y>-444.0</y><rotation>0.015966706</rotation><width>501.06387</width><height>16.0</height><uuid>ba6e2067-04c7-43c1-84e9-67a7cc3a3742</uuid></wall><wall><x>2264.0</x><y>-338.0</y><rotation>-1.5609927</rotation><width>204.0098</width><height>16.0</height><uuid>7ee9e08e-0242-4b98-8cd8-9979b3d6ae00</uuid></wall><wall><x>150.75427</x><y>-29.280197</y><rotation>-0.9221539</rotation><width>233.40309</width><height>24.0</height><uuid>e3e46fc7-f2e8-4f6d-91dc-fa63ffeb0a24</uuid></wall><wall><x>667.0</x><y>436.0</y><rotation>0.25204846</rotation><width>308.75555</width><height>24.0</height><uuid>154c6a76-a64e-4048-8a42-6212643894c2</uuid></wall><wall><x>1321.0</x><y>177.0</y><rotation>0.46718118</rotation><width>506.24896</width><height>24.0</height><uuid>8283cb84-d806-4d66-8e2c-68f1d1f88c34</uuid></wall><wall><x>1532.0</x><y>-112.0</y><rotation>0.51433843</rotation><width>335.3938</width><height>24.0</height><uuid>a2280164-8c83-46b3-b21c-eb53dcf84d55</uuid></wall><wall><x>1976.0</x><y>-47.0</y><rotation>-0.05660613</rotation><width>600.9626</width><height>24.0</height><uuid>3160224d-81e1-4eca-a212-75b5670d91ff</uuid></wall><wall><x>1882.0</x><y>259.0</y><rotation>-0.10425442</rotation><width>672.6522</width><height>24.0</height><uuid>f8b6de4d-0c71-484b-a234-02af7d1f7b29</uuid></wall><wall><x>2380.0</x><y>-163.0</y><rotation>-0.74961394</rotation><width>296.46753</width><height>24.0</height><uuid>e4e29eb8-bc9e-4204-a579-58f67768c740</uuid></wall><wall><x>2483.0</x><y>-409.0</y><rotation>-1.5742806</rotation><width>287.00174</width><height>24.0</height><uuid>dbed8415-73fe-4b86-a84f-db17d9a23370</uuid></wall><wall><x>2400.0034</x><y>-605.8547</y><rotation>-2.501823</rotation><width>214.40149</width><height>24.0</height><uuid>bdf11300-a608-4ec7-97ad-175494142458</uuid></wall><wall><x>2034.3789</x><y>-709.5778</y><rotation>-3.008221</rotation><width>564.00885</width><height>24.0</height><uuid>1d908ca7-8cdf-490c-a0f6-6a130009f69a</uuid></wall><wall><x>2514.0</x><y>47.0</y><rotation>-0.560804</rotation><width>673.1003</width><height>24.0</height><uuid>14b0c24d-465c-4ec8-ae1b-d19f47448222</uuid></wall><wall><x>2804.0</x><y>-353.0</y><rotation>-1.5332553</rotation><width>426.30035</width><height>24.0</height><uuid>bd1924da-57ee-49b9-bfa8-0f1a320360d3</uuid></wall><wall><x>2728.0</x><y>-713.0</y><rotation>-2.1303282</rotation><width>341.00146</width><height>24.0</height><uuid>70ebe2b7-dc86-47c5-88c3-a3d245a9b1f7</uuid></wall><wall><x>2535.0</x><y>-902.0</y><rotation>-2.834127</rotation><width>221.38202</width><height>24.0</height><uuid>0fa08ede-b102-47ae-8731-12ef2f0e7d06</uuid></wall><wall><x>2075.0</x><y>-1002.0</y><rotation>-2.9587505</rotation><width>709.5245</width><height>24.0</height><uuid>a21f6c1a-f74a-46eb-850d-3d07d2463f57</uuid></wall><wall><x>1759.3795</x><y>-597.87604</y><rotation>-1.6556981</rotation><width>330.18933</width><height>24.0</height><uuid>df75e0b0-824d-4ccc-bf66-53409675c5b2</uuid></wall><wall><x>1617.0</x><y>-1053.0</y><rotation>-0.07442369</rotation><width>228.63289</width><height>24.0</height><uuid>ff96c5f7-03ab-4abe-9c2f-134b212a9504</uuid></wall><wall><x>1465.0</x><y>-771.0</y><rotation>-1.4141945</rotation><width>596.29694</width><height>24.0</height><uuid>e9781b88-1543-4d05-a3a2-b203efd99a09</uuid></wall><trigger><x>1884.0</x><y>-351.0</y><width>309.64658</width><height>16.0</height><rotation>-1.635431</rotation><uuid>197d118d-4246-4d90-b1ed-0cf3db9dc74e</uuid></trigger><powerup><x>533.0</x><y>284.0</y><uuid>e3fd7fb8-baf0-4d42-bf9b-fc9b5a6902ee</uuid></powerup><powerup><x>1110.0</x><y>-36.0</y><uuid>68c552d2-8981-444a-8671-aa64a2e45140</uuid></powerup><powerup><x>1105.0</x><y>-472.0</y><uuid>faf2e150-d3da-4e6d-ab51-650acd671f78</uuid></powerup><powerup><x>1605.0</x><y>136.0</y><uuid>83737bf3-6327-4769-9572-6b538e292915</uuid></powerup><powerup><x>1843.0</x><y>107.0</y><uuid>bf950e5c-27a0-48e3-93ab-f43a45cb876d</uuid></powerup><powerup><x>2124.0</x><y>85.0</y><uuid>996089fb-3c6a-476c-afba-972e5a2d5def</uuid></powerup><powerup><x>2639.0</x><y>-196.0</y><uuid>a0116208-6a20-403a-9cc1-089a26b6b758</uuid></powerup><powerup><x>2642.0</x><y>-432.0</y><uuid>9100e341-6865-45c4-907f-556c5c25e0df</uuid></powerup><powerup><x>2602.0</x><y>-635.0</y><uuid>e4b13c9e-d652-49f2-b6cf-12a91d569397</uuid></powerup><powerup><x>1984.0</x><y>-869.0</y><uuid>386a1199-bacc-4fd8-9cd2-8f2e0487316f</uuid></powerup><powerup><x>1633.0</x><y>-901.0</y><uuid>c2f130a2-99b7-4e0f-8cb7-d08e5af91d21</uuid></powerup><powerup><x>1692.0</x><y>-310.0</y><uuid>87d207d6-3817-455a-b38b-d29f91d136c0</uuid></powerup></root><root><missleOrigin><x>0.0</x><y>0.0</y><angle>0.0</angle></missleOrigin><earthOrigin><x>7737.0</x><y>1371.0</y></earthOrigin><isCameraFollowsMissle>true</isCameraFollowsMissle><fileName>Deneb-273</fileName><creatorId>057dbe61</creatorId><uuid>8c47c77b-3265-4e55-bfae-003246ac3a00</uuid><isbuiltin>false</isbuiltin><wall><x>128.0</x><y>95.0</y><rotation>-0.09331112</rotation><width>375.63412</width><height>24.0</height><uuid>1ca29fed-9c7a-433e-a635-d0a3483b3b57</uuid></wall><wall><x>444.0</x><y>-48.0</y><rotation>-0.7342617</rotation><width>359.68042</width><height>24.0</height><uuid>f9cd8eeb-091b-46fb-8dc7-a1e0513cbac7</uuid></wall><wall><x>-81.0</x><y>-21.0</y><rotation>-1.6373645</rotation><width>270.59933</width><height>24.0</height><uuid>7c6d531c-9ff7-41d8-9a85-b290aa65577d</uuid></wall><wall><x>70.0</x><y>-185.0</y><rotation>-0.12184244</rotation><width>345.56186</width><height>24.0</height><uuid>eaf0afa5-e6be-404a-b736-c3e29bd8d368</uuid></wall><wall><x>314.9643</x><y>-304.61105</y><rotation>-0.9070172</rotation><width>256.45273</width><height>24.0</height><uuid>30066c76-416f-4a0d-b7f5-1442b3b6fcb9</uuid></wall><wall><x>643.0896</x><y>-373.73804</y><rotation>-1.241172</rotation><width>441.78387</width><height>24.0</height><uuid>7b4aeb32-8d54-4b5f-aac2-8abe483e17b7</uuid></wall><wall><x>402.28558</x><y>-504.8332</y><rotation>-1.4382448</rotation><width>211.85844</width><height>24.0</height><uuid>599c4f77-ece3-4536-98e3-601a88690542</uuid></wall><wall><x>346.24817</x><y>-734.8408</y><rotation>-2.0388622</rotation><width>303.66098</width><height>24.0</height><uuid>f70b8404-51e8-46ef-b581-17261ffaaafe</uuid></wall><wall><x>686.2143</x><y>-732.0912</y><rotation>-1.7126933</rotation><width>325.26913</width><height>24.0</height><uuid>f8170523-5800-4ae6-a1b5-95c83a971003</uuid></wall><wall><x>581.75006</x><y>-970.6714</y><rotation>-2.300696</rotation><width>229.45587</width><height>24.0</height><uuid>7e14202b-7fe9-465f-b5a2-e07b0fd8c773</uuid></wall><wall><x>221.21448</x><y>-913.1797</y><rotation>3.837575</rotation><width>145.58159</width><height>24.0</height><uuid>82e0de9e-8cc9-43ae-94a7-de9200a8ee3b</uuid></wall><wall><x>304.25024</x><y>-1216.9865</y><rotation>0.7100117</rotation><width>516.4262</width><height>24.0</height><uuid>53a123bd-4573-4024-b33e-d9876f5edbc9</uuid></wall><wall><x>-62.174667</x><y>-1022.8455</y><rotation>-2.8781261</rotation><width>472.29758</width><height>24.0</height><uuid>f7e5677f-0338-4e2e-ae23-27180021aa95</uuid></wall><wall><x>-345.7504</x><y>-1275.0209</y><rotation>-1.8962098</rotation><width>431.6538</width><height>24.0</height><uuid>cac1d5e3-34b4-4e68-87e3-2c2ad44610cf</uuid></wall><wall><x>47.99998</x><y>-1353.7845</y><rotation>2.7306821</rotation><width>152.71214</width><height>24.0</height><uuid>71600bb8-ccf4-4cdd-b17d-3d7007da98d6</uuid></wall><wall><x>-138.92847</x><y>-1608.4819</y><rotation>-1.9927393</rotation><width>600.6829</width><height>24.0</height><uuid>05f836b5-06d1-4e0c-9d07-7d48c8ae969c</uuid></wall><wall><x>-522.82153</x><y>-1551.3691</y><rotation>-2.474806</rotation><width>274.8745</width><height>24.0</height><uuid>fd11c2d3-64eb-4232-ae7a-2c115f8ca3ea</uuid></wall><wall><x>-385.35724</x><y>-1931.6567</y><rotation>0.42334685</rotation><width>277.49774</width><height>24.0</height><uuid>6f9e39fd-f2b4-4f86-a272-c8054d724204</uuid></wall><wall><x>-673.7921</x><y>-1976.2993</y><rotation>3.0874288</rotation><width>332.48758</width><height>24.0</height><uuid>4597ab29-7601-419a-af56-c40c73daaa70</uuid></wall><wall><x>-912.95917</x><y>-1868.5745</y><rotation>2.2401865</rotation><width>262.68802</width><height>24.0</height><uuid>e88ac69a-2138-4422-a83d-2f650679511b</uuid></wall><wall><x>-672.8217</x><y>-1560.19</y><rotation>2.2233412</rotation><width>154.80634</width><height>24.0</height><uuid>5a8d3015-ada8-49e6-9ffb-20ccd7da2872</uuid></wall><wall><x>-1037.1438</x><y>-1550.2522</y><rotation>1.7747391</rotation><width>449.3117</width><height>24.0</height><uuid>a09adbe3-37b3-4faa-9969-0f338c350a24</uuid></wall><wall><x>-738.00024</x><y>-1390.3531</y><rotation>1.6918236</rotation><width>223.63586</width><height>24.0</height><uuid>21d7c215-b740-4c07-8057-135728b6ca9f</uuid></wall><wall><x>-1065.7375</x><y>-1212.7524</y><rotation>1.3935107</rotation><width>243.82166</width><height>24.0</height><uuid>8c2e5a30-b606-43eb-a4b4-fdae3e2a095d</uuid></wall><wall><x>-946.9291</x><y>-967.2174</y><rotation>0.9260561</rotation><width>322.80026</width><height>24.0</height><uuid>260eaedc-7d4a-4208-959e-ee44ebda0e05</uuid></wall><wall><x>-707.64307</x><y>-788.5129</y><rotation>0.36555535</rotation><width>310.5173</width><height>24.0</height><uuid>4ade8967-9a69-4193-bffe-1aa10a8f3d7f</uuid></wall><wall><x>-729.96466</x><y>-1184.6085</y><rotation>1.3197936</rotation><width>201.30823</width><height>24.0</height><uuid>a8dbc00d-1997-4226-a69e-3d22e8a20c55</uuid></wall><wall><x>-373.71423</x><y>-956.5421</y><rotation>0.39075717</rotation><width>724.6213</width><height>24.0</height><uuid>2ee4eb10-8fa8-4cb5-b90d-06276d403420</uuid></wall><wall><x>-771.25055</x><y>-654.4507</y><rotation>2.7930124</rotation><width>447.93973</width><height>24.0</height><uuid>e8f77349-0967-43ab-a654-addbe086afba</uuid></wall><wall><x>-419.89346</x><y>-130.73969</y><rotation>1.4625257</rotation><width>416.43848</width><height>24.0</height><uuid>2d5bcb27-7054-4c7b-a820-adfd04d74269</uuid></wall><wall><x>-878.1052</x><y>-247.69055</y><rotation>1.3168573</rotation><width>676.70154</width><height>24.0</height><uuid>d37f1bad-5986-435e-88e6-a1ffa7445a65</uuid></wall><wall><x>-245.78622</x><y>239.98953</y><rotation>0.8464251</rotation><width>475.35355</width><height>24.0</height><uuid>2137f7a4-054f-418d-92a6-2cea4051327c</uuid></wall><wall><x>-503.8999</x><y>396.2394</y><rotation>0.82622516</rotation><width>866.2205</width><height>24.0</height><uuid>0d434493-a565-4ab4-8d28-b14af57bb500</uuid></wall><wall><x>12.249844</x><y>300.45508</y><rotation>-0.81524</rotation><width>284.38354</width><height>24.0</height><uuid>57cb7176-3aa8-43d1-ba6f-2796cfefbb4e</uuid></wall><wall><x>-65.21463</x><y>711.5346</y><rotation>-0.036650248</rotation><width>300.2016</width><height>24.0</height><uuid>efc292bb-085a-4497-9361-9c65dc19b87b</uuid></wall><wall><x>196.39276</x><y>535.3069</y><rotation>-0.97409827</rotation><width>414.65408</width><height>24.0</height><uuid>6a6fe78f-f2f9-4bbe-b62b-a1ce44b50fbe</uuid></wall><wall><x>796.0748</x><y>367.35483</y><rotation>0.002014096</rotation><width>993.002</width><height>24.0</height><uuid>3092a199-f19a-40b6-a59f-8eb593a451fb</uuid></wall><wall><x>267.60724</x><y>135.17604</y><rotation>-0.41375476</rotation><width>340.75357</width><height>24.0</height><uuid>d003d36d-98ac-4f8c-b62f-351008f71f77</uuid></wall><wall><x>867.4158</x><y>43.949524</y><rotation>-0.048007775</rotation><width>896.03235</width><height>24.0</height><uuid>1b60eb10-5fa3-4eb0-8363-568dd80d9c81</uuid></wall><wall><x>1390.3925</x><y>449.77176</y><rotation>0.710271</rotation><width>263.78778</width><height>24.0</height><uuid>0c3974e1-ef6d-430a-a13f-fda523cfea20</uuid></wall><wall><x>1493.9642</x><y>159.59366</y><rotation>0.6530494</rotation><width>460.821</width><height>24.0</height><uuid>2815c6c8-3081-42b7-86f6-2000d800dec9</uuid></wall><wall><x>1501.4554</x><y>660.5954</y><rotation>1.5670228</rotation><width>265.0019</width><height>24.0</height><uuid>19dff710-51dd-45cc-8e11-cda4cfc50389</uuid></wall><wall><x>1754.6783</x><y>460.44727</y><rotation>1.0601246</rotation><width>380.55222</width><height>24.0</height><uuid>ceb76eef-ba17-4c25-b511-94ed8396d05f</uuid></wall><wall><x>1787.1418</x><y>1040.0278</y><rotation>0.7291326</rotation><width>767.01044</width><height>24.0</height><uuid>e95ec63c-037d-4767-bcf7-5728ea00d5f6</uuid></wall><wall><x>1883.622</x><y>715.88257</y><rotation>1.2042221</rotation><width>189.00264</width><height>24.0</height><uuid>2e879e2f-2047-49a7-8598-76b3a86c13b5</uuid></wall><wall><x>2312.1438</x><y>1076.9067</y><rotation>-0.75889325</rotation><width>667.036</width><height>24.0</height><uuid>a2830ff8-a49f-430d-ad84-0dca1e341dec</uuid></wall><wall><x>2150.7527</x><y>743.05634</y><rotation>-0.2980764</rotation><width>503.75888</width><height>24.0</height><uuid>3de7d15e-1637-4d82-a34d-cac633706ad1</uuid></wall><wall><x>2553.216</x><y>626.597</y><rotation>-0.25580856</rotation><width>335.93155</width><height>24.0</height><uuid>b5399ff9-e942-4558-a39d-9dca405ef74c</uuid></wall><wall><x>2730.8198</x><y>993.10815</y><rotation>0.6282879</rotation><width>433.85022</width><height>24.0</height><uuid>6fee1252-8055-4bd7-902d-85b669df9894</uuid></wall><wall><x>2912.0706</x><y>739.8978</y><rotation>0.6935884</rotation><width>515.7567</width><height>24.0</height><uuid>b92b8235-eb9b-4c85-8816-959cd5c9b931</uuid></wall><wall><x>3102.0</x><y>1187.0</y><rotation>0.3366748</rotation><width>423.7924</width><height>24.0</height><uuid>45820023-c589-4799-bd6a-8b2059eeeb6a</uuid></wall><wall><x>3243.0</x><y>929.0</y><rotation>0.25012442</rotation><width>282.8003</width><height>24.0</height><uuid>237e8bd1-1415-46b8-85be-bcfd9623c63e</uuid></wall><wall><x>3490.4294</x><y>1251.2855</y><rotation>-0.043450896</rotation><width>391.3694</width><height>24.0</height><uuid>7ee107fa-b27d-4229-86a8-cad2031da04c</uuid></wall><wall><x>3548.0</x><y>950.0</y><rotation>-0.11779532</rotation><width>340.35864</width><height>24.0</height><uuid>4dfda9f5-1e60-4233-9eb7-52df8a3113e7</uuid></wall><wall><x>3854.0</x><y>865.0</y><rotation>-0.44036216</rotation><width>307.31906</width><height>24.0</height><uuid>dc4e494b-119b-4368-8cde-4bd323077d70</uuid></wall><wall><x>3890.432</x><y>1155.2064</y><rotation>-0.41329494</rotation><width>453.1545</width><height>24.0</height><uuid>a784ef42-7aa1-4b53-8efe-74eb93d008b9</uuid></wall><wall><x>4232.8447</x><y>931.02216</y><rotation>-0.7835149</rotation><width>375.47437</width><height>24.0</height><uuid>103d8af1-05c5-4883-874f-58834afa4c1f</uuid></wall><wall><x>4115.4336</x><y>681.61584</y><rotation>-0.76395893</rotation><width>362.82916</width><height>24.0</height><uuid>c1807585-f93d-4e8c-b4cc-b1fb44768338</uuid></wall><wall><x>4574.0</x><y>810.0</y><rotation>0.029876165</rotation><width>435.1942</width><height>24.0</height><uuid>34250013-abfc-4cf6-a30e-18ccb8c5d05c</uuid></wall><wall><x>4541.0</x><y>558.0</y><rotation>-0.0016778507</rotation><width>596.00085</width><height>24.0</height><uuid>4a1c6ec8-a8a7-4ebe-8cd6-a575f15d6095</uuid></wall><wall><x>4911.2197</x><y>875.6649</y><rotation>0.45215386</rotation><width>272.37106</width><height>24.0</height><uuid>98c13bf0-ef30-46a8-9543-f64b27b578f2</uuid></wall><wall><x>5019.0</x><y>612.0</y><rotation>0.32596993</rotation><width>374.73325</width><height>24.0</height><uuid>d0edd6c6-c982-480c-9318-6d3d31139205</uuid></wall><wall><x>5152.2466</x><y>1060.109</y><rotation>0.79749435</rotation><width>350.7506</width><height>24.0</height><uuid>aa3d2727-a0bb-4d3f-88e1-fc7ad19a4841</uuid></wall><wall><x>5322.782</x><y>816.9498</y><rotation>0.8491415</rotation><width>399.6198</width><height>24.0</height><uuid>94de5a20-4922-4e7c-ad14-92c4947fa06b</uuid></wall><wall><x>5329.926</x><y>1283.2733</y><rotation>1.0607604</rotation><width>231.45842</width><height>24.0</height><uuid>2b716788-9727-4a7f-85db-37508b54b478</uuid></wall><wall><x>5536.049</x><y>1106.7825</y><rotation>1.0403086</rotation><width>328.093</width><height>24.0</height><uuid>fe879bbd-8347-4439-93cb-6f2cec5ad673</uuid></wall><wall><x>5485.8887</x><y>1520.5017</y><rotation>0.9151007</rotation><width>344.4256</width><height>24.0</height><uuid>5e34891b-7554-48d7-abb8-ba587bebd2f4</uuid></wall><wall><x>5735.8896</x><y>1372.0337</y><rotation>0.82704073</rotation><width>339.70575</width><height>24.0</height><uuid>c37e7f3c-0f8f-43a7-a5e1-3fa6d172940d</uuid></wall><wall><x>5710.7764</x><y>1743.7162</y><rotation>0.62855744</rotation><width>307.83438</width><height>24.0</height><uuid>af6d8b82-95a9-4d88-9d97-acc2e0fd0a07</uuid></wall><wall><x>5946.0</x><y>1540.0</y><rotation>0.4143314</rotation><width>208.65521</width><height>24.0</height><uuid>f6e6b116-a838-484e-b9de-76488a4ac08f</uuid></wall><wall><x>5996.6064</x><y>1862.1165</y><rotation>0.1833285</rotation><width>334.60724</width><height>24.0</height><uuid>c058911d-a252-43f0-84fb-c970617ad2d2</uuid></wall><wall><x>6145.7144</x><y>1597.171</y><rotation>0.12774098</rotation><width>219.79082</width><height>24.0</height><uuid>3885b6ea-f3d9-4c0c-84df-61dd736e2be3</uuid></wall><wall><x>6307.322</x><y>1874.733</y><rotation>-0.11903589</rotation><width>303.14517</width><height>24.0</height><uuid>05159d4f-8372-41ec-9f6e-26e8b7ef394e</uuid></wall><wall><x>6369.822</x><y>1600.0824</y><rotation>-0.120748624</rotation><width>240.75299</width><height>24.0</height><uuid>d73d4619-f58d-4f3c-b5b2-e528021db391</uuid></wall><wall><x>6594.2085</x><y>1835.9132</y><rotation>-0.16154292</rotation><width>273.5617</width><height>24.0</height><uuid>b290725f-3167-4124-9461-639805b3b879</uuid></wall><wall><x>6608.0</x><y>1532.0</y><rotation>-0.41741723</rotation><width>251.60286</width><height>24.0</height><uuid>d8305b6a-cb7f-4971-aba6-ee5c056a4eba</uuid></wall><wall><x>6787.96</x><y>1422.4818</y><rotation>-0.75366277</rotation><width>178.28067</width><height>24.0</height><uuid>f055fbc7-e923-429e-b73b-2676c47a88f5</uuid></wall><wall><x>6870.9956</x><y>1299.2289</y><rotation>-1.2468086</rotation><width>141.35417</width><height>24.0</height><uuid>ba0bd9b9-9160-4093-b2f3-2d6b2d807a72</uuid></wall><wall><x>6837.96</x><y>1757.3033</y><rotation>-0.4830114</rotation><width>254.06496</width><height>24.0</height><uuid>a15720df-7c96-4cb9-8a6a-21d3594bbdab</uuid></wall><wall><x>7017.4253</x><y>1634.0502</y><rotation>-0.76667374</rotation><width>188.83061</width><height>24.0</height><uuid>ca83e861-67d6-45e4-a381-a10f67c88fb1</uuid></wall><wall><x>7470.9995</x><y>1556.4137</y><rotation>-0.038541317</rotation><width>778.5782</width><height>24.0</height><uuid>6773ffc2-2ca2-4fc6-a5ab-0a265127d96a</uuid></wall><wall><x>7376.632</x><y>1221.5891</y><rotation>-0.0060974853</rotation><width>984.0183</width><height>24.0</height><uuid>dc136120-0859-4505-b4fc-7e9e19c992c8</uuid></wall><wall><x>7875.31</x><y>1381.721</y><rotation>-1.5601301</rotation><width>375.02133</width><height>24.0</height><uuid>eef1e40b-e4d5-4324-8763-cc8eea22e85a</uuid></wall><wall><x>-19.10939</x><y>-745.3366</y><rotation>1.3769426</rotation><width>166.1114</width><height>24.0</height><uuid>871454a1-4807-4672-b20f-0f4781c55397</uuid></wall><wall><x>-28.930862</x><y>-608.4965</y><rotation>2.1054852</rotation><width>119.70798</width><height>24.0</height><uuid>f1cfd2c7-141c-4cc4-8302-59be7124bf27</uuid></wall><wall><x>-243.0</x><y>-450.0</y><rotation>2.5611324</rotation><width>441.27655</width><height>24.0</height><uuid>d3e9c72f-1085-4931-9671-6b4ca66bb175</uuid></wall><powerup><x>445.0</x><y>-242.0</y><uuid>801efcfd-2cf3-4f90-ac8d-ca5492c8a770</uuid></powerup><powerup><x>566.0</x><y>-642.0</y><uuid>1f35a441-8033-43d1-8b14-2ed779c68989</uuid></powerup><powerup><x>251.0</x><y>-1056.0</y><uuid>c9657176-213c-4771-aa80-47d0e486dbbd</uuid></powerup><powerup><x>-198.0</x><y>-1186.0</y><uuid>4fcbcd01-96d6-4133-9dc0-9a7d4ec76fdd</uuid></powerup><powerup><x>-382.0</x><y>-1712.0</y><uuid>f6cd65d2-92fa-4779-adaa-76ce374deb92</uuid></powerup><powerup><x>-741.0</x><y>-1806.0</y><uuid>8a877d1a-8566-464d-988d-4f897eb45568</uuid></powerup><powerup><x>-926.0</x><y>-1229.0</y><uuid>fbda1b36-c7e7-459c-be49-e9c110d56972</uuid></powerup><powerup><x>-447.0</x><y>-832.0</y><uuid>68df1ec9-f247-4d26-ae91-4d5845ff691e</uuid></powerup><powerup><x>-436.0</x><y>-586.0</y><uuid>b6a6af40-67ad-4c2e-a9be-feaaa369b9f6</uuid></powerup><powerup><x>-625.0</x><y>-32.0</y><uuid>1069b65d-c306-42e5-a1d4-a44d5338774f</uuid></powerup><powerup><x>-385.0</x><y>313.0</y><uuid>855eb846-eb1f-43ce-816e-8fe00895a30c</uuid></powerup><powerup><x>189.0</x><y>310.0</y><uuid>676b2ec7-3435-411b-bc13-9489cee5eee0</uuid></powerup><powerup><x>741.0</x><y>193.0</y><uuid>1e6c481a-715c-4722-ba93-167bcac3b217</uuid></powerup><powerup><x>1293.0</x><y>190.0</y><uuid>5677aa52-caa5-4fdd-9199-b9caf692744a</uuid></powerup><powerup><x>1662.0</x><y>677.0</y><uuid>861893d1-880c-46e8-bc84-009a999b4547</uuid></powerup><powerup><x>2163.0</x><y>941.0</y><uuid>d1685f22-0905-40f2-be0a-767efa50d1f4</uuid></powerup><powerup><x>2814.0</x><y>842.0</y><uuid>8dd50068-2ad8-4a8d-92fe-f58db705f682</uuid></powerup><powerup><x>3164.0</x><y>1071.0</y><uuid>fb5d34fd-a1ad-446a-9b18-7ea3e3986539</uuid></powerup><powerup><x>3559.0</x><y>1099.0</y><uuid>fda7a5d8-783f-42f2-a789-8430fdd784ea</uuid></powerup><powerup><x>3956.0</x><y>978.0</y><uuid>5cbbc4af-8faa-44ce-8a9b-0aa5d3e5cc39</uuid></powerup><powerup><x>4306.0</x><y>671.0</y><uuid>0a1ff1ce-8b7a-4295-b24b-7e8da3c130ec</uuid></powerup><powerup><x>4621.0</x><y>665.0</y><uuid>beed6465-5488-4244-89db-95dd5385fb28</uuid></powerup><powerup><x>5015.0</x><y>770.0</y><uuid>6bcd037d-0d07-45c5-b7dd-625d572192ba</uuid></powerup><powerup><x>5301.0</x><y>993.0</y><uuid>f63cfddf-f878-45bf-a2b6-796df3b1d7cd</uuid></powerup><powerup><x>5486.0</x><y>1331.0</y><uuid>0a3b84f8-f538-4ae1-8f1a-33777450f56d</uuid></powerup><powerup><x>5856.0</x><y>1673.0</y><uuid>bfdd4f95-3fdc-41c9-9962-8a323af24bea</uuid></powerup><powerup><x>6409.0</x><y>1733.0</y><uuid>fe3f3429-e031-4929-be42-5fac0fbd1d97</uuid></powerup><powerup><x>6918.0</x><y>1504.0</y><uuid>955ee119-191b-4b13-93a4-fc3b3b76243e</uuid></powerup><powerup><x>7349.0</x><y>1418.0</y><uuid>71c8c909-9c7f-42d4-a00b-1f39ac26de62</uuid></powerup></root><root><missleOrigin><x>1234.9788</x><y>226.74051</y><angle>-3.1415932</angle></missleOrigin><earthOrigin><x>1931.0</x><y>-979.0</y></earthOrigin><isCameraFollowsMissle>true</isCameraFollowsMissle><fileName>Jabbah-453</fileName><creatorId>057dbe61</creatorId><uuid>37a3060a-fd2e-4002-b8ee-f1fb449b2929</uuid><isbuiltin>false</isbuiltin><wall><x>-87.0</x><y>-127.0</y><rotation>5.8671207</rotation><width>118.02127</width><height>24.0</height><uuid>9e8a4964-32f1-4a10-af98-d7834a5673f2</uuid></wall><wall><x>39.5</x><y>-162.5</y><rotation>6.0880938</rotation><width>194.22926</width><height>24.0</height><uuid>506643e6-083a-4d8e-afc3-347bb8ffde7b</uuid></wall><wall><x>205.0</x><y>-153.5</y><rotation>0.3014955</rotation><width>195.7469</width><height>24.0</height><uuid>ab4c0f19-fdcb-44ba-a384-3db4c02acae9</uuid></wall><wall><x>359.0</x><y>-74.0</y><rotation>0.6435011</rotation><width>204.0</width><height>24.0</height><uuid>0e41faaf-9138-4c93-9c94-f4c679cd10c1</uuid></wall><wall><x>466.5</x><y>60.5</y><rotation>1.1554571</rotation><width>199.96022</width><height>24.0</height><uuid>528e8160-f4eb-4a0a-be98-c8806e7e9d98</uuid></wall><wall><x>427.5</x><y>352.0</y><rotation>2.3604138</rotation><width>191.5858</width><height>24.0</height><uuid>9f21a699-2448-4a0f-a2e9-a78c525e4efb</uuid></wall><wall><x>266.0</x><y>439.5</y><rotation>2.8691297</rotation><width>235.8136</width><height>24.0</height><uuid>c39ce28b-ea2d-42e4-936c-f673b22105e8</uuid></wall><wall><x>75.0</x><y>453.5</y><rotation>3.3030953</rotation><width>204.3469</width><height>24.0</height><uuid>2a489cfa-9d52-4b8d-878d-d1d780624aaa</uuid></wall><wall><x>-96.5</x><y>395.5</y><rotation>3.6268196</rotation><width>210.5315</width><height>24.0</height><uuid>e6cca0ac-2a91-4921-9594-a6feea3be7c4</uuid></wall><wall><x>-264.5</x><y>289.0</y><rotation>3.7766192</rotation><width>236.40762</width><height>24.0</height><uuid>7bba6554-1bb6-4c46-a14f-b05fee3d178a</uuid></wall><wall><x>-437.0</x><y>171.5</y><rotation>3.7012243</rotation><width>229.3217</width><height>24.0</height><uuid>6075e385-e45a-4d49-883f-55867840ee39</uuid></wall><wall><x>-594.5</x><y>45.0</y><rotation>3.9375167</rotation><width>225.53659</width><height>24.0</height><uuid>9cab0aa8-4778-4906-a0f3-64acf85113ec</uuid></wall><wall><x>-670.0</x><y>-108.0</y><rotation>4.6507387</rotation><width>186.30835</width><height>24.0</height><uuid>6a3cb123-f7fe-4b31-9090-cedd837eda5d</uuid></wall><wall><x>-641.5</x><y>-270.0</y><rotation>5.1045475</rotation><width>199.3083</width><height>24.0</height><uuid>b2db571d-cab5-4d0c-ae63-f92ac0863b41</uuid></wall><wall><x>-539.0</x><y>-422.0</y><rotation>5.4835024</rotation><width>222.0101</width><height>24.0</height><uuid>f2e7ef82-0cd6-4602-8007-2f5ebd5b8587</uuid></wall><wall><x>-351.0</x><y>-292.5</y><rotation>2.3073733</rotation><width>241.34074</width><height>24.0</height><uuid>4102aab0-3b63-45a8-985b-9c6a68b3b7c9</uuid></wall><wall><x>-277.0</x><y>-160.0</y><rotation>0.34000424</rotation><width>335.85254</width><height>24.0</height><uuid>735b828e-408d-4ba6-9d1c-fca5cc42fcf0</uuid></wall><wall><x>-130.0</x><y>-108.0</y><rotation>1.5707964</rotation><width>24.0</width><height>24.0</height><uuid>ec72ea32-d701-409f-b969-c2888dbdff3f</uuid></wall><wall><x>-14.0</x><y>176.0</y><rotation>0.9197196</rotation><width>182.40454</width><height>24.0</height><uuid>5c846306-1511-4810-a4b1-7ad4b9b8aa58</uuid></wall><wall><x>102.0</x><y>246.5</y><rotation>0.10985012</rotation><width>160.8247</width><height>24.0</height><uuid>ac75ad5a-8fc7-4788-b7fe-bcc6e9205011</uuid></wall><wall><x>226.5</x><y>202.5</y><rotation>5.54405</rotation><width>176.89867</width><height>24.0</height><uuid>18eef2ef-bbc1-4b84-ac55-673b43cd78fe</uuid></wall><wall><x>246.0</x><y>96.0</y><rotation>4.1202025</rotation><width>156.57451</width><height>24.0</height><uuid>4b769228-4eb2-4efe-aeb6-89dca55a2ba5</uuid></wall><wall><x>132.0</x><y>20.5</y><rotation>3.4017909</rotation><width>183.36436</width><height>24.0</height><uuid>1958ac1f-c5ab-4d83-a36c-87becedba5b1</uuid></wall><wall><x>-3.5</x><y>56.5</y><rotation>2.373584</rotation><width>186.65915</width><height>24.0</height><uuid>20493329-13bb-4732-87f9-991ba2479e51</uuid></wall><wall><x>-62.0</x><y>113.0</y><rotation>1.5707964</rotation><width>24.0</width><height>24.0</height><uuid>f7a0fdca-3559-44b9-a04a-70857c5d4409</uuid></wall><wall><x>-278.0</x><y>-373.0</y><rotation>1.5707964</rotation><width>24.0</width><height>24.0</height><uuid>ea3fa078-092b-46f4-9c16-2c96cb2daae6</uuid></wall><wall><x>-62.5</x><y>-611.5</y><rotation>5.44717</rotation><width>666.87634</width><height>24.0</height><uuid>50f6b997-65be-4aa5-975a-0673886ef02e</uuid></wall><wall><x>-247.5</x><y>-722.5</y><rotation>2.340709</rotation><width>663.3012</width><height>24.0</height><uuid>f3d2e35d-6310-424d-b329-f03d2b58bb1d</uuid></wall><wall><x>-470.0</x><y>-493.0</y><rotation>1.5707964</rotation><width>24.0</width><height>24.0</height><uuid>1136143c-47fe-489b-bc0f-1bb2f73813d4</uuid></wall><wall><x>153.0</x><y>-850.0</y><rotation>1.5707964</rotation><width>24.0</width><height>24.0</height><uuid>e30f1133-9c28-4097-a533-4a7011642ed6</uuid></wall><wall><x>237.0</x><y>-937.5</y><rotation>5.4773817</rotation><width>266.58813</width><height>24.0</height><uuid>19f4af88-db9f-4ee0-9956-fbb2da846719</uuid></wall><wall><x>316.0</x><y>-1096.5</y><rotation>4.6425724</rotation><width>167.34923</width><height>24.0</height><uuid>de6efe46-5c6e-4bef-a657-5c924eb3ccc8</uuid></wall><wall><x>93.0</x><y>-1350.5</y><rotation>3.8385825</rotation><width>592.6132</width><height>24.0</height><uuid>2d532036-e673-41b6-aeed-147a45a22bd9</uuid></wall><wall><x>-25.0</x><y>-952.0</y><rotation>1.5707964</rotation><width>24.0</width><height>24.0</height><uuid>31c78a40-d711-40ed-b36f-4f9450b02f8d</uuid></wall><wall><x>24.5</x><y>-1004.5</y><rotation>5.468384</rotation><width>168.31216</width><height>24.0</height><uuid>6ef99dca-a062-4066-a82b-f0823d8c007f</uuid></wall><wall><x>-147.0</x><y>-1268.0</y><rotation>3.9038467</rotation><width>635.10394</width><height>24.0</height><uuid>2a20567b-aae3-4a5d-9500-c20b0beb5a43</uuid></wall><wall><x>-368.0</x><y>-1479.0</y><rotation>1.5707964</rotation><width>24.0</width><height>24.0</height><uuid>689f8bcb-969f-47af-a5ad-dffb12565dc3</uuid></wall><wall><x>-393.5</x><y>-1577.5</y><rotation>4.4590673</rotation><width>227.49448</width><height>24.0</height><uuid>ad76bbbc-5df1-45f3-8669-65bffbe7aaac</uuid></wall><wall><x>-372.0</x><y>-1777.0</y><rotation>5.1479316</rotation><width>246.80035</width><height>24.0</height><uuid>f1009fa3-a7dd-47de-a408-c081b6e08263</uuid></wall><wall><x>-182.0</x><y>-1917.5</y><rotation>6.013682</rotation><width>320.7103</width><height>24.0</height><uuid>9f6fd162-d1dc-4237-98cc-c12d23138847</uuid></wall><wall><x>106.0</x><y>-1896.0</y><rotation>0.398214</rotation><width>338.61722</width><height>24.0</height><uuid>80a49386-0e7f-4622-8652-b8ddadfe0597</uuid></wall><wall><x>-125.0</x><y>-1533.0</y><rotation>1.5707964</rotation><width>24.0</width><height>24.0</height><uuid>5ed36a4c-76e3-4bcb-885b-56e1561b3738</uuid></wall><wall><x>-86.0</x><y>-1602.0</y><rotation>5.2268405</rotation><width>182.51814</width><height>24.0</height><uuid>ca4ec1b2-7c8a-41d9-8f57-2d3d39ce6728</uuid></wall><wall><x>348.5</x><y>-1498.5</y><rotation>0.41128245</rotation><width>886.9635</width><height>24.0</height><uuid>48788bd2-3509-4c2f-a601-bb87878be2b1</uuid></wall><wall><x>744.0</x><y>-1326.0</y><rotation>1.5707964</rotation><width>24.0</width><height>24.0</height><uuid>c36dbc6a-264f-4605-8352-f3d1a94c26ad</uuid></wall><wall><x>831.5</x><y>-1342.0</y><rotation>6.1023264</rotation><width>201.90166</width><height>24.0</height><uuid>aaec9efc-9d8b-4b3d-9906-d141ac19bf39</uuid></wall><wall><x>973.5</x><y>-1429.0</y><rotation>5.367063</rotation><width>203.01117</width><height>24.0</height><uuid>f4b4fe38-5c1a-4e97-9061-bf7d1a1f8e8e</uuid></wall><wall><x>867.0</x><y>-1872.5</y><rotation>4.3044233</rotation><width>835.6089</width><height>24.0</height><uuid>bd138a2f-6e72-4563-b8cd-7f9ca68068d5</uuid></wall><wall><x>251.0</x><y>-1835.0</y><rotation>1.5707964</rotation><width>24.0</width><height>24.0</height><uuid>bfec53fb-9079-4907-b6de-92961a48853b</uuid></wall><wall><x>451.5</x><y>-1749.5</y><rotation>0.4030846</rotation><width>459.93808</width><height>24.0</height><uuid>9928a4ab-c316-40ff-bb08-e0f539347d8f</uuid></wall><wall><x>524.5</x><y>-1966.0</y><rotation>4.312905</rotation><width>679.6226</width><height>24.0</height><uuid>e286feb8-0261-46b4-932d-2a52548b3093</uuid></wall><wall><x>397.0</x><y>-2268.0</y><rotation>1.5707964</rotation><width>24.0</width><height>24.0</height><uuid>238d9707-b4d0-49a8-b0b1-533960db4615</uuid></wall><wall><x>388.0</x><y>-2413.0</y><rotation>4.6503997</rotation><width>314.55807</width><height>24.0</height><uuid>05375389-bda2-4f56-8d42-0e59ffe3f20c</uuid></wall><wall><x>911.0</x><y>-2591.0</y><rotation>6.221234</rotation><width>1090.045</width><height>24.0</height><uuid>d4dfa941-3d80-4b75-aff5-629dc4ce49cc</uuid></wall><wall><x>1072.0</x><y>-2286.0</y><rotation>3.030036</rotation><width>760.57855</width><height>24.0</height><uuid>e53aef37-3d54-48df-bc63-fece765e363d</uuid></wall><wall><x>706.0</x><y>-2245.0</y><rotation>1.5707964</rotation><width>24.0</width><height>24.0</height><uuid>acb4e2ab-9d28-4116-b7ae-4d7934f0d8af</uuid></wall><wall><x>1438.0</x><y>-2327.0</y><rotation>1.5707964</rotation><width>24.0</width><height>24.0</height><uuid>610730f8-1210-4428-b29b-3068ea72eb57</uuid></wall><wall><x>1588.0</x><y>-2028.5</y><rotation>1.1051407</rotation><width>692.1385</width><height>24.0</height><uuid>57a44867-4450-4c20-8600-db32a9cb068a</uuid></wall><wall><x>1443.0</x><y>-2624.0</y><rotation>1.5707964</rotation><width>24.0</width><height>24.0</height><uuid>e9f2c07c-776b-46fd-ab71-01688e88ba2c</uuid></wall><wall><x>1560.5</x><y>-2576.5</y><rotation>0.38416934</rotation><width>277.47583</width><height>24.0</height><uuid>eaedf8f8-b9c9-4ce3-9929-d53f211a7af9</uuid></wall><wall><x>1826.5</x><y>-2132.0</y><rotation>1.212854</rotation><width>871.7293</width><height>24.0</height><uuid>81334a7b-31a3-4430-a156-4d92358f1d1d</uuid></wall><wall><x>1738.0</x><y>-1730.0</y><rotation>1.5707964</rotation><width>24.0</width><height>24.0</height><uuid>10ca4cb4-cb2b-4617-b1ef-d32b4b552099</uuid></wall><wall><x>1628.0</x><y>-1665.5</y><rotation>2.6112602</rotation><width>279.03137</width><height>24.0</height><uuid>2118cf90-cdcc-4fd3-8a48-0976ff4eb173</uuid></wall><wall><x>1444.5</x><y>-1498.5</y><rotation>2.1928892</rotation><width>276.2578</width><height>24.0</height><uuid>406f521a-cdff-4b16-9305-72f0bb0c6bc1</uuid></wall><wall><x>1320.0</x><y>-1247.0</y><rotation>1.9005789</rotation><width>338.97302</width><height>24.0</height><uuid>62c52531-3bb8-4c52-801e-7f1ef7f4479f</uuid></wall><wall><x>1975.0</x><y>-1735.0</y><rotation>1.5707964</rotation><width>24.0</width><height>24.0</height><uuid>0040d598-4f3a-4364-8816-e2d1c5285574</uuid></wall><wall><x>1967.0</x><y>-1608.5</y><rotation>1.6339533</rotation><width>277.50543</width><height>24.0</height><uuid>97764291-cab5-49b6-bede-931be069230d</uuid></wall><wall><x>1824.0</x><y>-1446.0</y><rotation>2.8809903</rotation><width>303.43515</width><height>24.0</height><uuid>b9b816de-9bea-4544-a3be-e7041e3775cb</uuid></wall><wall><x>1600.5</x><y>-1206.0</y><rotation>1.9801167</rotation><width>468.73926</width><height>24.0</height><uuid>96650814-cd64-4781-ad5d-6dc80b0787bc</uuid></wall><wall><x>1512.0</x><y>-1002.0</y><rotation>1.5707964</rotation><width>24.0</width><height>24.0</height><uuid>9a40ead3-a750-4a45-8efd-e1ce7e191acf</uuid></wall><wall><x>1488.5</x><y>-879.5</y><rotation>1.7603304</rotation><width>273.46744</width><height>24.0</height><uuid>a1cef092-d490-4e99-845e-e683b9e5d165</uuid></wall><wall><x>1543.5</x><y>-633.0</y><rotation>1.0064188</rotation><width>317.5183</width><height>24.0</height><uuid>2df46569-a36d-40a3-a424-962226064403</uuid></wall><wall><x>1800.0</x><y>-450.0</y><rotation>0.32006416</rotation><width>399.04666</width><height>24.0</height><uuid>6ea79dd2-6b9b-4c69-ad08-4658dc9edef3</uuid></wall><wall><x>2149.0</x><y>-462.5</y><rotation>5.8871493</rotation><width>394.6926</width><height>24.0</height><uuid>cae926c4-71a9-4ef4-99d7-d09ed060d2c4</uuid></wall><wall><x>2381.0</x><y>-687.5</y><rotation>5.0906467</rotation><width>354.35284</width><height>24.0</height><uuid>c17c448f-d005-4e10-9c14-fe4ef5431ce3</uuid></wall><wall><x>1269.0</x><y>-1098.0</y><rotation>1.5707964</rotation><width>24.0</width><height>24.0</height><uuid>e1d40c6e-d923-4569-be11-d90223cbdf93</uuid></wall><wall><x>1235.0</x><y>-963.5</y><rotation>1.8183974</rotation><width>301.4617</width><height>24.0</height><uuid>02442ef7-76bd-488f-82bb-e42198874ce0</uuid></wall><wall><x>1251.5</x><y>-650.5</y><rotation>1.2950883</rotation><width>395.01212</width><height>24.0</height><uuid>423e8e62-e58b-4797-87ff-ac559e6e332d</uuid></wall><wall><x>1443.5</x><y>-349.5</y><rotation>0.7135524</rotation><width>398.31805</width><height>24.0</height><uuid>6e0f510e-36bf-4d5e-b7cd-c5bd90e9bc05</uuid></wall><wall><x>1585.0</x><y>-227.0</y><rotation>1.5707964</rotation><width>24.0</width><height>24.0</height><uuid>8a302424-fd68-484c-90da-f2d3e5a939b6</uuid></wall><wall><x>1738.5</x><y>-173.0</y><rotation>0.33827007</rotation><width>349.44278</width><height>24.0</height><uuid>3584207a-5014-40cf-ae14-38cc8f3f074b</uuid></wall><wall><x>2051.0</x><y>-147.0</y><rotation>6.108872</rotation><width>346.89316</width><height>24.0</height><uuid>8e714ddf-c9e7-4dd9-bd2d-7d513427800b</uuid></wall><wall><x>2360.5</x><y>-274.5</y><rotation>5.699026</rotation><width>384.83514</width><height>24.0</height><uuid>c5ae7ed1-2eed-461e-a32d-b832b351c760</uuid></wall><wall><x>2609.0</x><y>-548.0</y><rotation>5.225324</rotation><width>423.39954</width><height>24.0</height><uuid>e49857d9-eb64-47bd-9437-6a1d88581f33</uuid></wall><wall><x>2442.0</x><y>-841.0</y><rotation>1.5707964</rotation><width>24.0</width><height>24.0</height><uuid>a7ac2a01-60b6-4cd5-9b15-eddd4517f6cd</uuid></wall><wall><x>2487.0</x><y>-965.5</y><rotation>5.0592237</rotation><width>288.76593</width><height>24.0</height><uuid>de8edb16-3707-4af6-8930-728d8b968181</uuid></wall><wall><x>2470.0</x><y>-1189.5</y><rotation>4.155146</rotation><width>258.47174</width><height>24.0</height><uuid>94b2420e-749b-431b-909a-1e1896ec1848</uuid></wall><wall><x>2270.0</x><y>-1388.0</y><rotation>3.7638955</rotation><width>363.67633</width><height>24.0</height><uuid>46046b95-c4b0-4c4e-a996-e357be29ca03</uuid></wall><wall><x>2707.0</x><y>-722.0</y><rotation>1.5707964</rotation><width>24.0</width><height>24.0</height><uuid>3fbbc2bf-9d35-487a-934f-a6973af9f684</uuid></wall><wall><x>2756.5</x><y>-913.5</y><rotation>4.965338</rotation><width>419.58817</width><height>24.0</height><uuid>11cde56c-0880-42b3-bf64-7947fea9a876</uuid></wall><wall><x>2734.0</x><y>-1264.0</y><rotation>4.287184</rotation><width>373.0845</width><height>24.0</height><uuid>cfc74492-af1b-41fd-8b2a-a3b48d18c381</uuid></wall><wall><x>2584.0</x><y>-1460.0</y><rotation>3.5845177</rotation><width>196.66151</width><height>24.0</height><uuid>86588c12-9995-407d-8110-c6b17797911a</uuid></wall><wall><x>2132.0</x><y>-1487.0</y><rotation>1.5707964</rotation><width>24.0</width><height>24.0</height><uuid>99919b9c-a1a5-4b55-912d-aad742ded365</uuid></wall><wall><x>2133.0</x><y>-1614.0</y><rotation>4.720263</rotation><width>278.00787</width><height>24.0</height><uuid>e7cccae1-5fea-46f9-9532-c94c1f3d118b</uuid></wall><wall><x>2420.5</x><y>-1944.5</y><rotation>5.665582</rotation><width>726.8357</width><height>24.0</height><uuid>dbada65c-74cb-44db-9f11-f87e2a2291b1</uuid></wall><wall><x>2506.0</x><y>-1497.0</y><rotation>1.5707964</rotation><width>24.0</width><height>24.0</height><uuid>6c3dfb09-f8ff-4981-b486-44a0228c6fa4</uuid></wall><wall><x>2501.5</x><y>-1567.5</y><rotation>4.648646</rotation><width>165.28694</width><height>24.0</height><uuid>ae05261a-b10a-4dc0-a352-45c87c4622d2</uuid></wall><wall><x>2636.0</x><y>-1757.0</y><rotation>5.575152</rotation><width>389.96176</width><height>24.0</height><uuid>18983561-f5e3-47ed-a626-7e376b130773</uuid></wall><wall><x>2707.0</x><y>-2148.0</y><rotation>1.5707964</rotation><width>24.0</width><height>24.0</height><uuid>14c95246-90ee-4c17-b197-1f91dde40e9b</uuid></wall><wall><x>2759.5</x><y>-2420.0</y><rotation>4.903059</rotation><width>578.0406</width><height>24.0</height><uuid>9cfc2ea5-6ef1-41bb-bbe9-7c01f308bca4</uuid></wall><wall><x>2954.5</x><y>-2676.0</y><rotation>0.11181226</rotation><width>310.79086</width><height>24.0</height><uuid>6bdf9a78-bf70-4076-a93a-a49211190d27</uuid></wall><wall><x>2775.0</x><y>-1876.0</y><rotation>1.5707964</rotation><width>24.0</width><height>24.0</height><uuid>38fc8e5c-6e8e-4a59-8ef2-2e464f564bae</uuid></wall><wall><x>2847.5</x><y>-1936.0</y><rotation>5.5918484</rotation><width>212.2153</width><height>24.0</height><uuid>17aa7286-7100-4dbf-aadd-3d0631492282</uuid></wall><wall><x>2964.0</x><y>-2229.5</y><rotation>4.8986416</rotation><width>499.2189</width><height>24.0</height><uuid>45dc9dd1-6149-4701-ac62-cea33414882c</uuid></wall><wall><x>3008.0</x><y>-2463.0</y><rotation>1.5707964</rotation><width>24.0</width><height>24.0</height><uuid>3b0446aa-40db-4f72-94de-cb4ce0c63433</uuid></wall><wall><x>3293.5</x><y>-2231.5</y><rotation>0.6813268</rotation><width>759.12585</width><height>24.0</height><uuid>b97d6452-8804-4e61-bfe3-5eb735dbb609</uuid></wall><wall><x>3097.0</x><y>-2660.0</y><rotation>1.5707964</rotation><width>24.0</width><height>24.0</height><uuid>bc9867d6-6b8a-4e5b-bccc-62662747b9f5</uuid></wall><wall><x>3503.5</x><y>-2325.5</y><rotation>0.6885362</rotation><width>1076.8676</width><height>24.0</height><uuid>20cebeee-9e91-4af5-bc36-5fb255f0ea39</uuid></wall><wall><x>3902.5</x><y>-1895.0</y><rotation>1.648763</rotation><width>216.58505</width><height>24.0</height><uuid>f36dedc8-0c2a-42f3-a739-f22df6d08c35</uuid></wall><wall><x>3579.0</x><y>-2000.0</y><rotation>1.5707964</rotation><width>24.0</width><height>24.0</height><uuid>85c9888b-421b-404c-ada3-0f9b3dcd0f14</uuid></wall><wall><x>3573.5</x><y>-1909.0</y><rotation>1.6311624</rotation><width>206.3321</width><height>24.0</height><uuid>74969fb6-5c45-4431-857e-86ef6a07f74b</uuid></wall><wall><x>3399.5</x><y>-1545.5</y><rotation>2.1245983</rotation><width>664.77606</width><height>24.0</height><uuid>ff485ac8-98bd-4f70-aa05-672d95fad1aa</uuid></wall><wall><x>3895.0</x><y>-1799.0</y><rotation>1.5707964</rotation><width>24.0</width><height>24.0</height><uuid>e2ab9295-aad9-46fc-91d8-26ea12ab4c39</uuid></wall><wall><x>3736.0</x><y>-1539.0</y><rotation>2.1196568</rotation><width>633.5277</width><height>24.0</height><uuid>1ff81363-aa5e-4626-b8ba-2c3632da8457</uuid></wall><wall><x>3586.0</x><y>-1217.0</y><rotation>1.426642</rotation><width>149.29965</width><height>24.0</height><uuid>086ad460-a1b1-4909-a344-38ca6ed28eab</uuid></wall><wall><x>3670.5</x><y>-1153.0</y><rotation>0.026483</rotation><width>175.05296</width><height>24.0</height><uuid>ad1ed77e-5bc6-41ca-8590-0c16a699ac89</uuid></wall><wall><x>3991.0</x><y>-1496.0</y><rotation>5.329891</rotation><width>870.286</width><height>24.0</height><uuid>adb46632-842d-4345-97a3-e0a469120393</uuid></wall><wall><x>3231.0</x><y>-1273.0</y><rotation>1.5707964</rotation><width>24.0</width><height>24.0</height><uuid>deddda78-ae6a-40f1-896f-ef53d8ff88ff</uuid></wall><wall><x>3257.5</x><y>-1157.0</y><rotation>1.3462021</rotation><width>261.97687</width><height>24.0</height><uuid>c53367ec-ee44-47b4-9a48-a676dd00ab95</uuid></wall><wall><x>3394.5</x><y>-948.0</y><rotation>0.69961417</rotation><width>312.85464</width><height>24.0</height><uuid>ba04bedf-d79b-4b5f-b61d-04cff046bf36</uuid></wall><wall><x>3711.5</x><y>-855.5</y><rotation>6.280744</rotation><width>437.00122</width><height>24.0</height><uuid>0d88c008-f850-40dd-9624-280363afb7be</uuid></wall><wall><x>3918.0</x><y>-856.0</y><rotation>1.5707964</rotation><width>24.0</width><height>24.0</height><uuid>ae60aa09-ad42-424b-ba31-fccb35cd87f7</uuid></wall><wall><x>4127.5</x><y>-1220.0</y><rotation>5.234636</rotation><width>863.9673</width><height>24.0</height><uuid>7ea00864-c48e-42fc-ba7f-23ccaee0a698</uuid></wall><wall><x>4435.5</x><y>-1608.0</y><rotation>6.145034</rotation><width>212.79883</width><height>24.0</height><uuid>71c64a39-fbb4-4d3d-9233-df109512d6d7</uuid></wall><wall><x>4539.5</x><y>-1533.5</y><rotation>1.4513674</rotation><width>200.2555</width><height>24.0</height><uuid>b2b0ca20-1987-48a0-80f4-3179b665ba27</uuid></wall><wall><x>4550.0</x><y>-1446.0</y><rotation>1.5707964</rotation><width>24.0</width><height>24.0</height><uuid>6191b9b7-292c-4e65-a259-bf8196e0288d</uuid></wall><wall><x>4790.5</x><y>-1444.5</y><rotation>0.006243522</rotation><width>505.00937</width><height>24.0</height><uuid>9b4ee9d4-f1fc-4467-8cfe-dcbbe59d762d</uuid></wall><wall><x>5119.5</x><y>-1536.5</y><rotation>5.4703217</rotation><width>281.48398</width><height>24.0</height><uuid>6c589f5c-a46e-4d9f-b6b3-7710388393b6</uuid></wall><wall><x>5201.5</x><y>-1804.5</y><rotation>4.675157</rotation><width>373.24203</width><height>24.0</height><uuid>55a3f301-11de-4946-85fe-a99ecdde0eb1</uuid></wall><wall><x>5195.0</x><y>-1979.0</y><rotation>1.5707964</rotation><width>24.0</width><height>24.0</height><uuid>252bd9fa-72b7-4f44-9a7f-fdcac25d9ef9</uuid></wall><wall><x>4872.5</x><y>-2210.5</y><rotation>3.7641847</rotation><width>817.9736</width><height>24.0</height><uuid>23859fa6-bb33-4765-ac7e-8d657c85fe74</uuid></wall><wall><x>4441.0</x><y>-1874.5</y><rotation>6.180675</rotation><width>444.2059</width><height>24.0</height><uuid>db24ccef-5887-4d5d-a935-e4b8530fa752</uuid></wall><wall><x>4713.0</x><y>-1835.5</y><rotation>0.76515806</rotation><width>198.69116</width><height>24.0</height><uuid>e7bf2b4d-f5fb-4064-93a0-5d49f5ccb722</uuid></wall><wall><x>4865.0</x><y>-1771.5</y><rotation>0.039305378</rotation><width>202.13759</width><height>24.0</height><uuid>a2e2fd88-d18e-4d84-8c0f-c7682d3392c9</uuid></wall><wall><x>4933.0</x><y>-1831.0</y><rotation>4.3906384</rotation><width>156.81566</width><height>24.0</height><uuid>d857eb97-aa71-4dcf-8909-f60d443867d3</uuid></wall><wall><x>4655.5</x><y>-2048.5</y><rotation>3.6837304</rotation><width>622.87396</width><height>24.0</height><uuid>002a4392-4b41-4426-a6d9-6c29971a5d3c</uuid></wall><wall><x>4399.0</x><y>-2203.0</y><rotation>1.5707964</rotation><width>24.0</width><height>24.0</height><uuid>178e97d2-bb29-4342-a6a8-892fc444767e</uuid></wall><wall><x>4316.5</x><y>-2094.5</y><rotation>2.2209003</rotation><width>296.60596</width><height>24.0</height><uuid>af64c926-88c7-4f89-b396-1855ef73e3a0</uuid></wall><wall><x>3724.0</x><y>-2417.0</y><rotation>5.070052</rotation><width>235.37643</width><height>24.0</height><uuid>6d2673ff-5b7e-4d1f-9d0d-5f86d5a71371</uuid></wall><wall><x>4550.0</x><y>-2442.0</y><rotation>1.5707964</rotation><width>24.0</width><height>24.0</height><uuid>ddfb8b6f-75b1-4404-b56b-7aaf03cf7d21</uuid></wall><wall><x>4408.0</x><y>-2425.0</y><rotation>3.0224411</rotation><width>310.02798</width><height>24.0</height><uuid>e4f33d16-6847-4f8a-9cd2-45decf3bfc1c</uuid></wall><wall><x>4150.0</x><y>-2461.5</y><rotation>3.573727</rotation><width>279.4858</width><height>24.0</height><uuid>7e1cd2a9-9289-4b2d-8124-8a18484f6a2c</uuid></wall><wall><x>4043.0</x><y>-2619.5</y><rotation>4.798301</rotation><width>233.77368</width><height>24.0</height><uuid>05b4b65d-6c3c-48fd-9427-0e4c2794ecb5</uuid></wall><wall><x>3687.0</x><y>-2318.0</y><rotation>1.5707964</rotation><width>24.0</width><height>24.0</height><uuid>c1abd619-087c-430e-8fce-81f7a46c7e1d</uuid></wall><wall><x>3798.0</x><y>-2299.5</y><rotation>0.16514865</rotation><width>249.06221</width><height>24.0</height><uuid>526eeb19-5c84-4e03-8675-21cb4a0a10d3</uuid></wall><wall><x>4029.0</x><y>-2215.5</y><rotation>0.49963874</rotation><width>297.4246</width><height>24.0</height><uuid>11082e18-0d1d-444b-856b-6cb6c2655ccc</uuid></wall><wall><x>4191.5</x><y>-2068.0</y><rotation>1.0926219</rotation><width>208.7187</width><height>24.0</height><uuid>7805bfd8-7bc3-49e1-a3bb-6deeec359dbb</uuid></wall><wall><x>4234.0</x><y>-1986.0</y><rotation>1.5707964</rotation><width>24.0</width><height>24.0</height><uuid>ccdec11e-eccf-4dc6-bf7a-9818e60abdd9</uuid></wall><wall><x>3761.0</x><y>-2516.0</y><rotation>1.5707964</rotation><width>24.0</width><height>24.0</height><uuid>2a19a572-b82a-4799-b66c-4c0cc04cd70d</uuid></wall><wall><x>3477.5</x><y>-2669.5</y><rotation>3.6378448</rotation><width>668.77747</width><height>24.0</height><uuid>6000d33c-ef28-4aa3-a84c-2169ef74eb17</uuid></wall><wall><x>3010.5</x><y>-2841.0</y><rotation>3.2393725</rotation><width>392.76144</width><height>24.0</height><uuid>62488df4-6986-4892-b124-a5ff0696d4f8</uuid></wall><wall><x>4052.0</x><y>-2724.0</y><rotation>1.5707964</rotation><width>24.0</width><height>24.0</height><uuid>9418e57b-bc42-429c-a27f-1281c03ccf2b</uuid></wall><wall><x>3886.5</x><y>-2784.5</y><rotation>3.4920607</rotation><width>376.42303</width><height>24.0</height><uuid>2c08deda-90ef-495e-93f9-9922afb26197</uuid></wall><wall><x>3471.0</x><y>-2962.0</y><rotation>3.579314</rotation><width>576.0471</width><height>24.0</height><uuid>d4901e82-42e4-444c-8913-bbed4832371a</uuid></wall><wall><x>3221.0</x><y>-3079.0</y><rotation>1.5707964</rotation><width>24.0</width><height>24.0</height><uuid>24d1513f-0395-4ff2-9a6e-a400d19e21c0</uuid></wall><wall><x>2892.5</x><y>-3106.0</y><rotation>3.2236004</rotation><width>683.21545</width><height>24.0</height><uuid>900d128f-d817-493a-98ca-f57dcd463a70</uuid></wall><wall><x>2485.5</x><y>-2835.0</y><rotation>1.8283678</rotation><width>640.3319</width><height>24.0</height><uuid>2c62fe6c-9056-481a-b551-ad9028945a1f</uuid></wall><wall><x>2827.0</x><y>-2859.0</y><rotation>1.5707964</rotation><width>24.0</width><height>24.0</height><uuid>601000cb-4f2e-458a-80dc-897b251d0b91</uuid></wall><wall><x>2766.0</x><y>-2857.5</y><rotation>3.1170077</rotation><width>146.03688</width><height>24.0</height><uuid>a27b0529-a863-49f8-bc03-b45be45cbc6a</uuid></wall><wall><x>2641.0</x><y>-2600.0</y><rotation>1.815775</rotation><width>551.7575</width><height>24.0</height><uuid>6344f538-5bf4-4d13-a655-d1573a206a70</uuid></wall><wall><x>2407.0</x><y>-2537.0</y><rotation>1.5707964</rotation><width>24.0</width><height>24.0</height><uuid>82fdf116-760b-4929-a04f-66c3ce682308</uuid></wall><wall><x>2155.0</x><y>-2225.5</y><rotation>2.2509942</rotation><width>825.3395</width><height>24.0</height><uuid>48420fbd-2f3f-46e0-9d29-6a3fc29edbc4</uuid></wall><wall><x>2577.0</x><y>-2344.0</y><rotation>1.5707964</rotation><width>24.0</width><height>24.0</height><uuid>0cf514e3-3a4e-4844-afa4-d99201975173</uuid></wall><wall><x>2412.0</x><y>-2080.5</y><rotation>2.130248</rotation><width>645.795</width><height>24.0</height><uuid>cd31bdf3-8012-45b7-bde6-8726bdd02f5f</uuid></wall><wall><x>2035.0</x><y>-1223.5</y><rotation>0.005889822</rotation><width>194.00294</width><height>24.0</height><uuid>38bf6633-ee9e-42a8-8112-8e85b434cee6</uuid></wall><wall><x>2191.0</x><y>-1129.5</y><rotation>0.9213328</rotation><width>258.80417</width><height>24.0</height><uuid>1d5635f8-79cb-4027-9a4d-04ef4da0b080</uuid></wall><wall><x>2246.0</x><y>-928.0</y><rotation>1.7178746</rotation><width>242.3575</width><height>24.0</height><uuid>e3b639ba-1559-4cb9-b200-c1fd4ddbb22d</uuid></wall><wall><x>2113.5</x><y>-784.0</y><rotation>2.841888</rotation><width>267.87085</width><height>24.0</height><uuid>cdbe8f4c-1b06-4d33-82c2-5a03b6ae9acc</uuid></wall><wall><x>1993.0</x><y>-812.0</y><rotation>4.64997</rotation><width>152.24976</width><height>24.0</height><uuid>a4597cf4-4db1-4f35-bd71-2de2eaa583ba</uuid></wall><wall><x>2051.5</x><y>-893.0</y><rotation>6.01761</rotation><width>153.5415</width><height>24.0</height><uuid>a0a763c8-25e3-410b-a4e0-8db0dd29784b</uuid></wall><wall><x>2114.0</x><y>-910.0</y><rotation>1.5707964</rotation><width>24.0</width><height>24.0</height><uuid>6e8bdc06-f965-4e9f-85b9-c6dff899d7ef</uuid></wall><wall><x>2098.5</x><y>-1004.0</y><rotation>4.548966</rotation><width>214.53871</width><height>24.0</height><uuid>887a7930-92cd-457c-820a-c8c64fcacdf7</uuid></wall><wall><x>1933.5</x><y>-1097.5</y><rotation>3.138245</rotation><width>323.00168</width><height>24.0</height><uuid>6260c65d-759d-4d45-83a0-3876520ee534</uuid></wall><wall><x>1715.5</x><y>-871.0</y><rotation>1.8650923</rotation><width>496.30603</width><height>24.0</height><uuid>5b5e13f7-c25e-423f-932c-2028a131323b</uuid></wall><wall><x>1803.5</x><y>-530.0</y><rotation>0.6337177</rotation><width>412.41858</width><height>24.0</height><uuid>c64d1ce6-14f9-4c5d-8a0a-8d9bb416e599</uuid></wall><wall><x>1950.0</x><y>-1224.0</y><rotation>1.5707964</rotation><width>24.0</width><height>24.0</height><uuid>0a69dcce-9f59-4464-ac24-5946fafdd8d8</uuid></wall><wall><x>1953.5</x><y>-1356.0</y><rotation>4.738898</rotation><width>288.09277</width><height>24.0</height><uuid>7ed58e08-5745-4058-89f5-6a02c78a6d11</uuid></wall><wall><x>913.5</x><y>327.0</y><rotation>0.052270293</rotation><width>866.1502</width><height>24.0</height><uuid>eea20c95-826a-4826-948d-77e2852be1f5</uuid></wall><wall><x>1337.5</x><y>232.5</y><rotation>4.742423</rotation><width>257.10513</width><height>24.0</height><uuid>62852626-a0fa-47fc-9345-0c252bd1b58d</uuid></wall><wall><x>923.0</x><y>124.5</y><rotation>3.1212597</rotation><width>860.17285</width><height>24.0</height><uuid>7e484118-adcf-4b85-a5a8-88104e9e7288</uuid></wall><powerup><x>525.0</x><y>240.0</y><uuid>ac1794ab-b089-4233-b402-e8a198fefda8</uuid></powerup><powerup><x>248.0</x><y>329.0</y><uuid>0fd200d3-a36b-4341-9994-10d38c82b838</uuid></powerup><powerup><x>-85.0</x><y>312.0</y><uuid>066a8f94-158a-4dd3-8c9f-d35856c1b0ff</uuid></powerup><powerup><x>-298.0</x><y>56.0</y><uuid>1eb66f6f-fed9-4efc-8fef-2d50a998380e</uuid></powerup><powerup><x>-357.0</x><y>-520.0</y><uuid>b8739676-528d-44dd-8492-854dcea9dc57</uuid></powerup><powerup><x>-101.0</x><y>-807.0</y><uuid>4347e6e0-861b-407a-b62a-74cad761de17</uuid></powerup><powerup><x>206.0</x><y>-1093.0</y><uuid>890cb0f2-39a8-4a32-b116-6b729fd0ee9d</uuid></powerup><powerup><x>-83.0</x><y>-1352.0</y><uuid>73239f78-8555-47f7-8ffa-41a39594652f</uuid></powerup><powerup><x>-267.0</x><y>-1525.0</y><uuid>9051d07f-360c-47e2-86e5-fd8c269bcb4b</uuid></powerup><powerup><x>78.0</x><y>-1780.0</y><uuid>648e8549-03ae-4e5d-b2b5-bdaaca19fc7d</uuid></powerup><powerup><x>375.0</x><y>-1640.0</y><uuid>96c6fe83-8887-4cd2-bcd3-84eb88cec1d4</uuid></powerup><powerup><x>656.0</x><y>-1518.0</y><uuid>9b54e66a-3468-4455-ba55-74d525b8068c</uuid></powerup><powerup><x>690.0</x><y>-1940.0</y><uuid>7b1313d3-53ea-4712-8210-974b5b35f4ef</uuid></powerup><powerup><x>581.0</x><y>-2209.0</y><uuid>0df41beb-0703-4f90-a3e2-fc499669aaa2</uuid></powerup><powerup><x>863.0</x><y>-2448.0</y><uuid>ef1627be-fff5-4ecb-8d62-3412ac592e49</uuid></powerup><powerup><x>1175.0</x><y>-2479.0</y><uuid>f13649fb-2558-4932-9712-44a11cbeabb4</uuid></powerup><powerup><x>1533.0</x><y>-2453.0</y><uuid>9e01670b-51bb-4753-8216-3e36d6e32749</uuid></powerup><powerup><x>1730.0</x><y>-2002.0</y><uuid>6e6acd4c-f305-4b00-bbd3-bc152e66b0ea</uuid></powerup><powerup><x>1608.0</x><y>-1510.0</y><uuid>49ddc42c-7660-442d-9e4b-f1a8e30460b3</uuid></powerup><powerup><x>1384.0</x><y>-1061.0</y><uuid>9c6397c8-5393-452c-843d-bfcc0985f98f</uuid></powerup><powerup><x>1350.0</x><y>-700.0</y><uuid>e4f505d4-4f6d-4c13-bed6-aa6d6a414ff8</uuid></powerup><powerup><x>1589.0</x><y>-344.0</y><uuid>f9c662b9-c8ce-4956-8ea1-703ce8c499e6</uuid></powerup><powerup><x>2186.0</x><y>-315.0</y><uuid>eeaf52b0-654a-4281-8561-a5ee1563f080</uuid></powerup><powerup><x>2539.0</x><y>-651.0</y><uuid>bb04f194-63f2-41a1-8a27-bbc5ca445668</uuid></powerup><powerup><x>2653.0</x><y>-1071.0</y><uuid>5d62b30b-3228-41b9-a90d-072889eea10f</uuid></powerup><powerup><x>2296.0</x><y>-1599.0</y><uuid>4f105808-442b-4cb9-a54c-9b501b982170</uuid></powerup><powerup><x>3216.0</x><y>-2442.0</y><uuid>d774232a-a1f8-45ff-ac88-b3e0cfc03c49</uuid></powerup><powerup><x>3397.0</x><y>-2292.0</y><uuid>7701857b-0ed3-454d-868b-d94aca712dd8</uuid></powerup><powerup><x>3574.0</x><y>-2142.0</y><uuid>3a8edb9c-b32a-452e-8115-569d7b424770</uuid></powerup><powerup><x>3690.0</x><y>-1780.0</y><uuid>4fe2efe1-21b2-4c7d-bf0e-ae30051764bd</uuid></powerup><powerup><x>3508.0</x><y>-1495.0</y><uuid>8cf6be0a-0fa6-4fd7-a6cf-b4ddaae583ca</uuid></powerup><powerup><x>3420.0</x><y>-1176.0</y><uuid>c383e441-02a4-45a4-91b1-c3d0c7a82d56</uuid></powerup><powerup><x>3937.0</x><y>-1178.0</y><uuid>02d5afca-6d0b-4a4b-9163-2793c39fb07e</uuid></powerup><powerup><x>4101.0</x><y>-1457.0</y><uuid>8622c298-04cc-4fe8-ade7-1250bc8861fc</uuid></powerup><powerup><x>4296.0</x><y>-1737.0</y><uuid>63a79203-84ee-4618-a82a-35b59e18b958</uuid></powerup><powerup><x>4867.0</x><y>-2043.0</y><uuid>eb64ad0a-0281-4bb4-a6e8-d0decb8b2a0d</uuid></powerup><powerup><x>4611.0</x><y>-2243.0</y><uuid>ea0320e0-89d9-474c-94ff-28cbbb0b75b3</uuid></powerup><powerup><x>3902.0</x><y>-2439.0</y><uuid>db735433-2afb-48c9-8e93-281797b53ad4</uuid></powerup><powerup><x>3846.0</x><y>-2704.0</y><uuid>fa9ea242-9afd-41ab-b81a-2acc6e30ad14</uuid></powerup><powerup><x>3478.0</x><y>-2774.0</y><uuid>ac4b793f-8e17-4962-96ab-3f84cfdf67fd</uuid></powerup><powerup><x>3078.0</x><y>-2977.0</y><uuid>a6cc10f7-2262-4609-bfe9-96a27b99bb81</uuid></powerup><powerup><x>2812.0</x><y>-2999.0</y><uuid>9b3aa071-258d-463d-b0a7-4aa7ddf12901</uuid></powerup><powerup><x>2570.0</x><y>-2789.0</y><uuid>8cfed0fa-7c16-4cf3-8ae7-392943c8510e</uuid></powerup><powerup><x>2439.0</x><y>-2380.0</y><uuid>bb0c1f73-2c8d-4d8a-8d82-87c045ad1e05</uuid></powerup><powerup><x>2207.0</x><y>-2062.0</y><uuid>d52eda80-d7c4-4a68-a63d-f10102b35ddf</uuid></powerup></root><root><missleOrigin><x>0.0</x><y>0.0</y><angle>0.47123885</angle></missleOrigin><earthOrigin><x>2980.0706</x><y>159.25195</y></earthOrigin><isCameraFollowsMissle>true</isCameraFollowsMissle><fileName>meboodle-242</fileName><creatorId>057dbe61</creatorId><uuid>05be337b-31dc-48bf-8c63-3376b4479b10</uuid><isbuiltin>false</isbuiltin><wall><x>-148.0</x><y>-97.0</y><rotation>-0.8706053</rotation><width>540.06757</width><height>24.0</height><uuid>8e6ea73d-c84d-4153-bd84-7c9783f33f17</uuid></wall><wall><x>-170.0</x><y>123.0</y><rotation>0.218669</rotation><width>319.02542</width><height>24.0</height><uuid>3801f7cc-e61a-4321-8554-cd4b2562e1e1</uuid></wall><wall><x>110.5</x><y>279.0</y><rotation>0.73745024</rotation><width>392.8265</width><height>24.0</height><uuid>309534bd-ae15-4828-99f2-f2a2eb82608c</uuid></wall><wall><x>373.5</x><y>435.5</y><rotation>0.25147834</rotation><width>285.2164</width><height>24.0</height><uuid>3c5d250f-504c-40a5-8183-c72a2b61d553</uuid></wall><wall><x>629.5</x><y>420.0</y><rotation>5.9282284</rotation><width>300.21912</width><height>24.0</height><uuid>9461179d-f5f5-4117-9b20-af43fbba8d1d</uuid></wall><wall><x>820.5</x><y>198.5</y><rotation>5.0530376</rotation><width>392.15485</width><height>24.0</height><uuid>ed9355aa-25e8-42d2-a8b0-8ff6cc9e793c</uuid></wall><wall><x>93.5</x><y>-294.5</y><rotation>0.4607658</rotation><width>179.18376</width><height>24.0</height><uuid>f6fac139-1bfb-4151-9350-16b38526ca7d</uuid></wall><wall><x>237.0</x><y>-184.0</y><rotation>0.7987307</rotation><width>236.1509</width><height>24.0</height><uuid>654e50c2-2cae-4b7a-b311-5d0e34452006</uuid></wall><wall><x>362.5</x><y>-32.5</y><rotation>0.972172</rotation><width>206.78403</width><height>24.0</height><uuid>ed5cf7a8-332d-4ff2-b9a1-120142e406e3</uuid></wall><wall><x>486.0</x><y>47.5</y><rotation>0.062418513</rotation><width>168.28098</width><height>24.0</height><uuid>6bd363af-5ea9-4576-a845-44b91e7795a8</uuid></wall><wall><x>631.5</x><y>3.0</y><rotation>5.695183</rotation><width>200.67201</width><height>24.0</height><uuid>c8e36e75-4ee9-4c78-9843-be1a4c574ee3</uuid></wall><wall><x>706.5</x><y>-154.5</y><rotation>4.726213</rotation><width>241.02074</width><height>24.0</height><uuid>1e71293d-75cf-4df5-aa30-d19be3275b95</uuid></wall><wall><x>708.0</x><y>-263.0</y><rotation>1.5707964</rotation><width>24.0</width><height>24.0</height><uuid>c6f6da9b-87ad-4c2d-b8c6-178c3e33c06c</uuid></wall><wall><x>1250.0</x><y>-294.0</y><rotation>6.226052</rotation><width>1109.7716</width><height>24.0</height><uuid>e4140016-1021-4ffc-856b-8a7975680413</uuid></wall><wall><x>1891.5</x><y>-233.0</y><rotation>0.74625367</rotation><width>295.0295</width><height>24.0</height><uuid>99100d18-461b-4158-b396-0171eb282d5d</uuid></wall><wall><x>1987.0</x><y>115.0</y><rotation>1.58642</rotation><width>536.0625</width><height>24.0</height><uuid>7442f1e8-b062-4480-b0f4-d84906b51a44</uuid></wall><wall><x>882.0</x><y>25.0</y><rotation>1.5707964</rotation><width>24.0</width><height>24.0</height><uuid>cde1ce06-0fc7-40db-9771-ef17b1d20bf2</uuid></wall><wall><x>1247.0</x><y>-17.5</y><rotation>6.167269</rotation><width>758.93195</width><height>24.0</height><uuid>2f4fc7fe-42e5-412e-b961-7eab381bb408</uuid></wall><wall><x>1682.5</x><y>13.0</y><rotation>0.802818</rotation><width>226.97044</width><height>24.0</height><uuid>ef9d10c8-f535-45e9-b277-4f6a1b7877fa</uuid></wall><wall><x>1752.0</x><y>268.5</y><rotation>1.5762757</rotation><width>389.0055</width><height>24.0</height><uuid>0d9b0b79-1f44-4365-acdb-5b66facc760c</uuid></wall><wall><x>1751.0</x><y>451.0</y><rotation>1.5707964</rotation><width>24.0</width><height>24.0</height><uuid>57f96961-362c-4c35-8c85-bb716ab27800</uuid></wall><wall><x>1937.0</x><y>704.5</y><rotation>0.93779105</rotation><width>652.83466</width><height>24.0</height><uuid>23af833d-06b3-4a05-9399-e1bc401d0227</uuid></wall><wall><x>1983.0</x><y>371.0</y><rotation>1.5707964</rotation><width>24.0</width><height>24.0</height><uuid>5843bf3c-9aa2-42d0-9f69-02f035e440bd</uuid></wall><wall><x>2213.5</x><y>640.0</y><rotation>0.86232316</rotation><width>732.4949</width><height>24.0</height><uuid>f7a34c5b-157c-47c9-a22a-7a6001846907</uuid></wall><wall><x>2123.0</x><y>958.0</y><rotation>1.5707964</rotation><width>24.0</width><height>24.0</height><uuid>7a5763a3-f354-4dc7-be41-e07b3a9e2844</uuid></wall><wall><x>2231.0</x><y>1089.0</y><rotation>0.88133705</rotation><width>363.55853</width><height>24.0</height><uuid>141bc286-9abe-4546-aa9e-b214570a36af</uuid></wall><wall><x>2405.0</x><y>1286.0</y><rotation>0.7853982</rotation><width>210.6762</width><height>24.0</height><uuid>8721861c-f308-4edb-b599-fb9974bd8a9b</uuid></wall><wall><x>2576.0</x><y>1381.0</y><rotation>0.26947245</rotation><width>241.86234</width><height>24.0</height><uuid>9333ef59-88e4-4bc8-b8d6-42d01b197da9</uuid></wall><wall><x>2746.5</x><y>1381.5</y><rotation>5.872779</rotation><width>166.86357</width><height>24.0</height><uuid>e0de4c37-8512-4fcf-9813-7127a861b279</uuid></wall><wall><x>2893.5</x><y>1285.0</y><rotation>5.587844</rotation><width>236.28519</width><height>24.0</height><uuid>f768e609-1b4f-4f45-9a1a-e65c0a23ca9a</uuid></wall><wall><x>2989.5</x><y>1122.5</y><rotation>4.8646407</rotation><width>215.21193</width><height>24.0</height><uuid>d7861bf2-d360-4820-90de-de38af19abde</uuid></wall><wall><x>3004.0</x><y>926.0</y><rotation>4.712389</rotation><width>228.0</width><height>24.0</height><uuid>94c1101c-e382-4b42-aa0e-7f022c43560c</uuid></wall><wall><x>2444.0</x><y>909.0</y><rotation>1.5707964</rotation><width>24.0</width><height>24.0</height><uuid>e5aaff2d-c78c-4dcd-a1da-80b092c8e80f</uuid></wall><wall><x>2484.0</x><y>978.5</y><rotation>1.0485567</rotation><width>184.37769</width><height>24.0</height><uuid>c10933fc-18a7-405f-ab18-e432d0068752</uuid></wall><wall><x>2580.5</x><y>1099.0</y><rotation>0.73428</rotation><width>176.2268</width><height>24.0</height><uuid>1260fe5f-b2d5-4efd-b643-2866a2f86c0d</uuid></wall><wall><x>2721.0</x><y>1111.5</y><rotation>5.853423</rotation><width>208.8053</width><height>24.0</height><uuid>7fe2901b-1155-4b9c-b12e-ca5791113ee5</uuid></wall><wall><x>2815.0</x><y>943.5</y><rotation>4.789456</rotation><width>283.77106</width><height>24.0</height><uuid>8d18301f-7758-4ffd-b341-399d5fb566d8</uuid></wall><wall><x>2836.5</x><y>681.0</y><rotation>4.7986407</rotation><width>290.99252</width><height>24.0</height><uuid>12d9a73e-7444-4a28-8223-ea2b64a2e485</uuid></wall><wall><x>3004.0</x><y>824.0</y><rotation>1.5707964</rotation><width>24.0</width><height>24.0</height><uuid>038df405-9211-4487-896c-a3f7e549c5d5</uuid></wall><wall><x>3050.5</x><y>446.0</y><rotation>4.8347898</rotation><width>785.6987</width><height>24.0</height><uuid>b1e61e56-cfac-4761-a91d-58a8bca36109</uuid></wall><wall><x>2995.5</x><y>54.0</y><rotation>3.2786586</rotation><width>228.92194</width><height>24.0</height><uuid>d9c0096e-9441-4d77-999f-002527d821f0</uuid></wall><wall><x>2871.0</x><y>294.0</y><rotation>1.6611012</rotation><width>534.0784</width><height>24.0</height><uuid>856d1b13-119b-4df5-8a47-8fecddc7e13e</uuid></wall><wall><x>2848.0</x><y>548.0</y><rotation>1.5707964</rotation><width>24.0</width><height>24.0</height><uuid>b7f99ff4-bcef-4b50-9ead-dc4802d5e267</uuid></wall><powerup><x>347.0</x><y>217.0</y><uuid>d6d0bdbb-6b5c-47d4-81c6-ad0a76cd88ce</uuid></powerup><powerup><x>690.0</x><y>299.0</y><uuid>6681251f-00cf-4487-9e40-93eb50b5f66d</uuid></powerup><powerup><x>1159.0</x><y>-131.0</y><uuid>a8a468e4-c8d6-4119-bcf2-55d4226cb40a</uuid></powerup><powerup><x>1603.0</x><y>-178.0</y><uuid>20cddb8b-bbce-48ee-acbc-1aca78c10674</uuid></powerup></root></mazes>";



    static String[] statsTracked = new String[]{"statsWon",
            "statsPoints", "statsDeath", "statsPowerup",
            "statsUpvote", "statsDownvote", "statsMissleHit",
            "statsCreateWall", "statsCreatePowerup", "statsPerfection","consecutiveNoDeaths"};


    /*
     * Keep track of various user actions
     */
    public static void statsWon(){
        if(LevelFactory.isCreator && !Globals.instance().isDebug() && !LevelUtils.getCreatorId().equals("057dbe61"))
            return;
        Globals.instance().getPrefs()
                .edit()
                .putInt(statsTracked[0], 1+Globals.instance().getPrefs()
                        .getInt(statsTracked[0], 0)).commit();

    }

    public static int statsGetWon(){
        return Globals.instance().getPrefs().getInt(statsTracked[0], 0);
    }





    public static void statsPoints(int points){
        if(LevelFactory.isCreator && !Globals.instance().isDebug() && !LevelUtils.getCreatorId().equals("057dbe61"))
            return;
        Globals.instance().getPrefs()
                .edit()
                .putInt(statsTracked[1], points+Globals.instance().getPrefs()
                        .getInt(statsTracked[1], 0)).commit();
    }

    public static int statsGetPoints(){
        return Globals.instance().getPrefs().getInt(statsTracked[1], 0);
    }





    public static void statsDeath(){
        if(LevelFactory.isCreator && !Globals.instance().isDebug() && !LevelUtils.getCreatorId().equals("057dbe61"))
            return;
        Globals.instance().getPrefs()
                .edit()
                .putInt(statsTracked[2], 1+Globals.instance().getPrefs()
                        .getInt(statsTracked[2], 0)).commit();
    }

    public static int statsGetDeath(){
        return Globals.instance().getPrefs().getInt(statsTracked[2], 0);
    }





    public static void statsPowerup(){
        if(LevelFactory.isCreator && !Globals.instance().isDebug() && !LevelUtils.getCreatorId().equals("057dbe61"))
            return;
        Globals.instance().getPrefs()
                .edit()
                .putInt(statsTracked[3], 1+Globals.instance().getPrefs()
                        .getInt(statsTracked[3], 0)).commit();
    }

    public static int statsGetPowerup(){
        return Globals.instance().getPrefs().getInt(statsTracked[3], 0);
    }





    public static void statsUpvote(){
        Globals.instance().getPrefs()
                .edit()
                .putInt(statsTracked[4], 1+Globals.instance().getPrefs()
                        .getInt(statsTracked[4], 0)).commit();
    }

    public static int statsGetUpvote(){
        return Globals.instance().getPrefs().getInt(statsTracked[4], 0);
    }





    public static void statsDownvote(){
        Globals.instance().getPrefs()
                .edit()
                .putInt(statsTracked[5], 1+Globals.instance().getPrefs()
                        .getInt(statsTracked[5], 0)).commit();
    }

    public static int statsGetDownvote(){
        return Globals.instance().getPrefs().getInt(statsTracked[5], 0);
    }






    public static void statsMissleHit(){
        if(LevelFactory.isCreator && !Globals.instance().isDebug() && !LevelUtils.getCreatorId().equals("057dbe61"))
            return;
        Globals.instance().getPrefs()
                .edit()
                .putInt(statsTracked[6], 1+Globals.instance().getPrefs()
                        .getInt(statsTracked[6], 0)).commit();
    }

    public static int statsGetMissleHit(){
        return Globals.instance().getPrefs().getInt(statsTracked[6], 0);
    }






    public static void statsCreateWall(){
        Globals.instance().getPrefs()
                .edit()
                .putInt(statsTracked[7], 1+Globals.instance().getPrefs()
                        .getInt(statsTracked[7], 0)).commit();
    }

    public static int statsGetCreateWall(){
        return Globals.instance().getPrefs().getInt(statsTracked[7], 0);
    }






    public static void statsCreatePowerup(){
        Globals.instance().getPrefs()
                .edit()
                .putInt(statsTracked[8], 1+Globals.instance().getPrefs()
                        .getInt(statsTracked[8], 0)).commit();
    }

    public static int statsGetCreatePowerup(){
        return Globals.instance().getPrefs().getInt(statsTracked[8], 0);
    }






    public static void statsPerfection(){
        if(LevelFactory.isCreator && !Globals.instance().isDebug() && !LevelUtils.getCreatorId().equals("057dbe61"))
            return;
        Globals.instance().getPrefs()
                .edit().putInt(statsTracked[9],  1+Globals.instance().getPrefs()
                .getInt(statsTracked[9],0)).commit();
        //if(MyGameActivity.mGoogleApiClient.isConnected())
            //Games.Achievements.unlock(MyGameActivity.mGoogleApiClient, Globals.ACHIEVEMENT_PERFECTION);
    }

    public static int statsGetPerfection(){
        return Globals.instance().getPrefs().getInt(statsTracked[9], 0);
    }





    public static void statsClearConsecutiveNoDeaths(){
        if(LevelFactory.isCreator && !Globals.instance().isDebug() && !LevelUtils.getCreatorId().equals("057dbe61"))
            return;
        Globals.instance().getPrefs()
                .edit().putInt(statsTracked[10],  0).commit();
    }

    public static void statsConsecutiveNoDeaths(){
        if(LevelFactory.isCreator && !Globals.instance().isDebug() && !LevelUtils.getCreatorId().equals("057dbe61"))
            return;
        Globals.instance().getPrefs()
                .edit().putInt(statsTracked[10],  1+Globals.instance().getPrefs()
                .getInt(statsTracked[10],0)).commit();
        int noDeathsStreak = statsGetConsecutiveNoDeaths();
/*
        if(MyGameActivity.mGoogleApiClient.isConnected()){
            if(noDeathsStreak>=40)
                Games.Achievements.unlock(MyGameActivity.mGoogleApiClient, Globals.ACHIEVEMENT_NO_DEATHS_40);
            else if(noDeathsStreak>=20)
                Games.Achievements.unlock(MyGameActivity.mGoogleApiClient, Globals.ACHIEVEMENT_NO_DEATHS_20);
            else if(noDeathsStreak>=10)
                Games.Achievements.unlock(MyGameActivity.mGoogleApiClient, Globals.ACHIEVEMENT_NO_DEATHS_10);
        }
*/

    }


    public static int statsGetConsecutiveNoDeaths(){
        return Globals.instance().getPrefs().getInt(statsTracked[10], 0);
    }






    public static String getStatsText(){
        StringBuilder sb = new StringBuilder();
        sb.append("Won "+LevelUtils.statsGetWon()+"\n");
        sb.append("Deaths "+LevelUtils.statsGetDeath()+"\n");
        sb.append("Points "+LevelUtils.statsGetPoints()+"\n");
        sb.append("Collisions "+LevelUtils.statsGetMissleHit()+"\n");
        sb.append("Powerups "+LevelUtils.statsGetPowerup()+"\n");
        sb.append("Walls made "+LevelUtils.statsGetCreateWall()+"\n");
        sb.append("Powerups made "+LevelUtils.statsGetCreatePowerup()+"\n");
        sb.append("Upvotes "+LevelUtils.statsGetUpvote()+"\n");
        sb.append("Downvotes "+LevelUtils.statsGetDownvote()+"\n");
        sb.append("Perfection "+LevelUtils.statsGetPerfection()+"\n");
        sb.append("wins w/ no deaths "+LevelUtils.statsGetConsecutiveNoDeaths());


        return sb.toString();
    }


    public static Set<String> getVotes(){
        return Globals.instance().getPrefs().getStringSet("votes", new HashSet<String>());
    }

    public static void setVotes(Set<String> votes){
        Globals.instance().getPrefs().edit().putStringSet("votes", votes).commit();
    }


    /*
     * This is used when saving to oort cloud. Its just easier to read it
     * from the disk again than it is to juggle output and getting the
     * String back.
     */
    public static String loadXmlFromDisk(String fileName, Context context){

        try {
            FileInputStream fis = context.openFileInput(fileName);
            InputStreamReader isr = new InputStreamReader(fis,"UTF-8");
            char[] data = new char[fis.available()];
            isr.read(data);
            String xml = new String(data);
            return xml;

        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch(UnsupportedEncodingException f){
            f.printStackTrace();
        } catch(IOException g){
            g.printStackTrace();
        }

        return "";
    }

    public static void loadFromFile(String fileName, Context context){
        Level.asteroids = new ArrayList<S_Asteroid>();
        Level.satellites = new ArrayList<S_Satellite>();
        Level.walls = new ArrayList<S_Wall>();
        Level.triggers = new ArrayList<S_Trigger>();
        Level.powerups = new ArrayList<S_Powerup>();
        Level.warps    = new ArrayList<S_Warp>();


        FileInputStream fis = null;
        InputStreamReader isr = null;
        String data = null;

        try{
            fis = context.openFileInput(fileName);
            isr = new InputStreamReader(fis);
            char[] inputBuffer = new char[fis.available()];
            isr.read(inputBuffer);
            data = new String(inputBuffer);
            isr.close();
            fis.close();

            if(Globals.instance().isDebug()){
                Log.w("Level.load.file","loaded "+data.length()+" characters from file with name "+fileName);
                Log.w("Level.load.file",data);
            }
        }
        catch(Exception e){
            Log.e("Level.load.file",e.toString());
        }


        loadLevelIntoMemory(data, context);

    }

    /*
     * context is my game activity
     *
     */
    public static void loadLevelIntoMemory(String data, Context context){

        Level.asteroids = new ArrayList<S_Asteroid>();
        Level.satellites = new ArrayList<S_Satellite>();
        Level.walls = new ArrayList<S_Wall>();
        Level.triggers = new ArrayList<S_Trigger>();
        Level.powerups = new ArrayList<S_Powerup>();
        Level.warps    = new ArrayList<S_Warp>();



        Level.xml = data;


	    /*
	     * converting the String data to XML format
	     * so that the DOM parser understand it as an XML input.
	     */

        try{
            InputStream is = new ByteArrayInputStream(data.getBytes("UTF-8"));

            //ArrayList<XmlData> xmlDataList = new ArrayList<XmlData>();


            //XmlData xmlDataObj;
            DocumentBuilderFactory dbf;
            DocumentBuilder db;
            NodeList items = null;
            Document dom;

            dbf = DocumentBuilderFactory.newInstance();
            db = dbf.newDocumentBuilder();
            dom = db.parse(is);
            // normalize the document
            dom.getDocumentElement().normalize();

//	     ______            __                __
//	    /_  __/__  ___    / /  ___ _  _____ / /
//	     / / / _ \/ _ \  / /__/ -_) |/ / -_) /
//	    /_/  \___/ .__/ /____/\__/|___/\__/_/
//	            /_/

            try{
                items = dom.getElementsByTagName("missleOrigin");

                if (items != null && items.getLength() > 0) {
                    Node item = items.item(0);
                    NodeList attr = item.getChildNodes();
                    Level.missleOrigin = new Vector2();

                    if (attr != null && attr.getLength() > 0) {
                        for (int i = 0; i < attr.getLength(); i++) {
                            Node subItem = attr.item(i);
                            String name = subItem.getNodeName();

                            if (name!=null && name.equalsIgnoreCase("x")) {
                                Level.missleOrigin.x = Float.parseFloat(subItem
                                        .getTextContent());
                            }
                            else if (name!=null && name.equalsIgnoreCase("y")) {
                                Level.missleOrigin.y = Float.parseFloat(subItem
                                        .getTextContent());
                            }
                            else if(name!=null && name.equalsIgnoreCase("angle")){
                                Level.missleOriginAngle = Float.parseFloat(subItem.getTextContent());
                            }
                        }
                    }
                }
            }
            catch(Exception e){
                Log.e("Level.load.xml.missleOrigin", e.toString());
            }

            items = dom.getElementsByTagName("earthOrigin");

            if(items!=null && items.getLength()>0){
                Node item = items.item(0);
                NodeList attr = item.getChildNodes();
                Level.earthOrigin = new Vector2();

                for(int i = 0; i<attr.getLength(); i++){
                    Node subItem = attr.item(i);
                    String name = subItem.getNodeName();


                    if(name.equalsIgnoreCase("x")){
                        Level.earthOrigin.x = Float.parseFloat(subItem.getTextContent());
                    }
                    else if(name.equalsIgnoreCase("y")){
                        Level.earthOrigin.y = Float.parseFloat(subItem.getTextContent());
                    }
                }
            }

            items = dom.getElementsByTagName("isCameraFollowsMissle");

            if(items!=null && items.getLength()>0){
                Node item = items.item(0);
                Level.isCameraFollowsMissle = Boolean.parseBoolean(item.getTextContent());

            }

            items = dom.getElementsByTagName("fileName");

            if(items!=null && items.getLength()>0){
                Node item = items.item(0);
                Level.fileName = item.getTextContent();
            }

            items = dom.getElementsByTagName("creatorId");

            if(items!=null && items.getLength()>0){
                Node item = items.item(0);
                Level.creatorId = item.getTextContent();
            }

            items = dom.getElementsByTagName("uuid");

            if(items!=null && items.getLength()>0){
                Node item = items.item(0);
                Level.uuid = item.getTextContent();
            }

            items = dom.getElementsByTagName("isbuiltin");

            if(items!=null && items.getLength()>0){
                Node item = items.item(0);
                Level.isBuiltIn = Boolean.parseBoolean(item.getTextContent());
            }

//		    ____     __      _____ __
//		   / __/__ _/ /____ / / (_) /____ ___
//		  _\ \/ _ `/ __/ -_) / / / __/ -_|_-<
//		 /___/\_,_/\__/\__/_/_/_/\__/\__/___/
//

            items = dom.getElementsByTagName("satellite");

            for(int i=0;i<items.getLength(); i++){

                Node item = items.item(i);
                NodeList attr = item.getChildNodes();
                S_Satellite sat = new S_Satellite();

                for(int j=0; j<attr.getLength(); j++){
                    Node subItem = attr.item(j);
                    String name = subItem.getNodeName();


                    if(name.equalsIgnoreCase("x")){
                        sat.x = Float.parseFloat(subItem.getTextContent());
                    }
                    else if(name.equalsIgnoreCase("y")){
                        sat.y = Float.parseFloat(subItem.getTextContent());
                    }
                    else if(name.equalsIgnoreCase("uuid")){
                        sat.uniqueId = subItem.getTextContent();
                    }
                }

                Level.satellites.add(sat);
            }


//		   _      __     ____
//		  | | /| / /__ _/ / /__
//		  | |/ |/ / _ `/ / (_-<
//		  |__/|__/\_,_/_/_/___/

            items = dom.getElementsByTagName("wall");

            for(int i=0;i<items.getLength(); i++){

                Node item = items.item(i);
                NodeList attr = item.getChildNodes();

                S_Wall wall = new S_Wall();

                for(int j=0; j<attr.getLength(); j++){
                    Node subItem = attr.item(j);
                    String name = subItem.getNodeName();


                    if(name.equalsIgnoreCase("x")){
                        wall.x = Float.parseFloat(subItem.getTextContent());
                    }
                    else if(name.equalsIgnoreCase("y")){
                        wall.y = Float.parseFloat(subItem.getTextContent());
                    }
                    else if(name.equalsIgnoreCase("rotation")){
                        wall.rotation = Float.parseFloat(subItem.getTextContent());
                    }
                    else if(name.equalsIgnoreCase("width")){
                        wall.width = Float.parseFloat(subItem.getTextContent());
                    }
                    else if(name.equalsIgnoreCase("height")){
                        wall.height = Float.parseFloat(subItem.getTextContent());
                    }
                    else if(name.equalsIgnoreCase("uuid")){
                        wall.uniqueId = subItem.getTextContent();
                    }



                }

                Level.walls.add(wall);
            }

//	     ______    _
//	    /_  __/___(_)__ ____ ____ _______
//	     / / / __/ / _ `/ _ `/ -_) __(_-<
//	    /_/ /_/ /_/\_, /\_, /\__/_/ /___/
//	              /___//___/

            items = dom.getElementsByTagName("trigger");

            for(int i=0;i<items.getLength(); i++){

                Node item = items.item(i);
                NodeList attr = item.getChildNodes();

                S_Trigger trigger = new S_Trigger();

                for(int j=0; j<attr.getLength(); j++){
                    Node subItem = attr.item(j);
                    String name = subItem.getNodeName();


                    if(name.equalsIgnoreCase("x")){
                        trigger.x = Float.parseFloat(subItem.getTextContent());
                    }
                    else if(name.equalsIgnoreCase("y")){
                        trigger.y = Float.parseFloat(subItem.getTextContent());
                    }
                    else if(name.equalsIgnoreCase("rotation")){
                        trigger.rotation = Float.parseFloat(subItem.getTextContent());
                    }
                    else if(name.equalsIgnoreCase("width")){
                        trigger.width = Float.parseFloat(subItem.getTextContent());
                    }
                    else if(name.equalsIgnoreCase("height")){
                        trigger.height = Float.parseFloat(subItem.getTextContent());
                    }
                    else if(name.equalsIgnoreCase("text")){
                        trigger.text = subItem.getTextContent();
                    }
                    else if(name.equalsIgnoreCase("zoom")){
                        trigger.zoom = Float.parseFloat(subItem.getTextContent());
                    }
                    else if(name.equalsIgnoreCase("uuid")){
                        trigger.uniqueId = subItem.getTextContent();
                    }



                }

                Level.triggers.add(trigger);
            }

            items = dom.getElementsByTagName("powerup");
            for(int i=0;i<items.getLength(); i++){

                Node item = items.item(i);
                NodeList attr = item.getChildNodes();
                S_Powerup powerup = new S_Powerup();

                for(int j=0; j<attr.getLength(); j++){
                    Node subItem = attr.item(j);
                    String name = subItem.getNodeName();


                    if(name.equalsIgnoreCase("x")){
                        powerup.x = Float.parseFloat(subItem.getTextContent());
                    }
                    else if(name.equalsIgnoreCase("y")){
                        powerup.y = Float.parseFloat(subItem.getTextContent());
                    }
                    else if(name.equalsIgnoreCase("type")){
                        powerup.type = subItem.getTextContent();
                    }
                    else if(name.equalsIgnoreCase("points")){
                        powerup.points = Integer.parseInt(subItem.getTextContent());
                    }
                    else if(name.equalsIgnoreCase("uuid")){
                        powerup.uniqueId = subItem.getTextContent();
                    }


                }

                Level.powerups.add(powerup);
            }

            //WARPS!!!!!

            items = dom.getElementsByTagName("warp");
            for(int i=0;i<items.getLength(); i++){

                Node item = items.item(i);
                NodeList attr = item.getChildNodes();
                S_Warp warp = new S_Warp();

                for(int j=0; j<attr.getLength(); j++){
                    Node subItem = attr.item(j);
                    String name = subItem.getNodeName();


                    if(name.equalsIgnoreCase("x")){
                        warp.x = Float.parseFloat(subItem.getTextContent());
                    }
                    else if(name.equalsIgnoreCase("y")){
                        warp.y = Float.parseFloat(subItem.getTextContent());
                    }
                    else if(name.equalsIgnoreCase("name")){
                        warp.name = subItem.getTextContent();
                    }
                    else if(name.equalsIgnoreCase("connectsTo")){
                        warp.connectsTo = subItem.getTextContent();
                    }
                    else if(name.equalsIgnoreCase("rotation")){
                        warp.rotation = Float.parseFloat(subItem.getTextContent());
                    }
                    else if(name.equalsIgnoreCase("uuid")){
                        warp.uniqueId = subItem.getTextContent();
                    }


                }

                Level.warps.add(warp);
            }

//	    ___       __               _    __
//	   / _ | ___ / /____ _______  (_)__/ /__
//	  / __ |(_-</ __/ -_) __/ _ \/ / _  (_-<
//	 /_/ |_/___/\__/\__/_/  \___/_/\_,_/___/

            items = dom.getElementsByTagName("asteroid");

            for(int i=0;i<items.getLength(); i++){

                Node item = items.item(i);
                NodeList attr = item.getChildNodes();
                S_Asteroid ast = new S_Asteroid();

                for(int j=0; j<attr.getLength(); j++){
                    Node subItem = attr.item(j);
                    String name = subItem.getNodeName();


                    if(name.equalsIgnoreCase("x")){
                        ast.x = Float.parseFloat(subItem.getTextContent());
                    }
                    else if(name.equalsIgnoreCase("y")){
                        ast.y = Float.parseFloat(subItem.getTextContent());
                    }
                    else if(name.equalsIgnoreCase("releaseTime")){
                        ast.releaseTime = Float.parseFloat(subItem.getTextContent());
                    }
                    else if(name.equalsIgnoreCase("uuid")){
                        ast.uniqueId = subItem.getTextContent();
                    }


                }

                Level.asteroids.add(ast);
            }



            is.close();
        }

        catch(Exception e){
            Log.e("Level.load.xml", e.toString());
        }

        //if(Globals.instance().isDebug()){
        //Log.w("Level.load","Loaded level: "+Level.toString());
        //}

        //return level;
    }

    public static String grabXmlFromLocalDb(MyGameActivity mg){

        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        renderXmlToStream(baos, mg);
        try{
            baos.close();
        }
        catch(IOException e){
            e.printStackTrace();
        }
        return baos.toString();
    }


    public static String saveToLocalUserDb(MyGameActivity mg){


        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        renderXmlToStream(baos, mg);
        Level.xml = baos.toString();
        try{
            baos.close();
        }
        catch(IOException e){
            e.printStackTrace();
        }

        //return mg.myDB.insertOrUpdateLevel();
        return mg.myDB.insertOrUpdateUserLevel();


    }

    public static void deleteLevel(MyGameActivity mg){
        //mg.myDB.deleteLevel();
        mg.myDB.deleteUserLevel();
    }

    public static void renderXmlToStream( OutputStream stream, Context context) {


        try {

            //Log.w("Level.save", "Saving :"+Level.toString());

            //FileOutputStream fos;


            //File file = context.getDir("com.joao024.mystery3d.me", Context.MODE_WORLD_WRITEABLE);

			/*File f = context.getFilesDir();

			File file = context.getFileStreamPath(f.getAbsolutePath()+"/"+fileName);

			if(Globals.instance().isDebug()){
				Log.w("Starting Level Editor","Saving level to path:"+file.getAbsolutePath());
			}

			fos = new FileOutputStream(file, false);
			*/
            //fos = context.openFileOutput(Level.fileName, Context.MODE_PRIVATE);

            final XmlSerializer xml = Xml.newSerializer();

            xml.setOutput(stream, "UTF-8");
            xml.startDocument(null, Boolean.valueOf(true));
            //xml.setFeature(
            //"http://xmlpull.org/v1/doc/features.html#indent-output",
            // true);

            xml.startTag(null, "root");


//		     ______            __                __
//		    /_  __/__  ___    / /  ___ _  _____ / /
//		     / / / _ \/ _ \  / /__/ -_) |/ / -_) /
//		    /_/  \___/ .__/ /____/\__/|___/\__/_/
//		            /_/


            xml.startTag(null,"missleOrigin");
            xml.startTag(null, "x");
            xml.text(Level.missleOrigin.x+"");
            xml.endTag(null, "x");
            xml.startTag(null, "y");
            xml.text(Level.missleOrigin.y+"");
            xml.endTag(null, "y");
            xml.startTag(null, "angle");
            xml.text(Level.missleOriginAngle+"");
            xml.endTag(null,"angle");
            xml.endTag(null, "missleOrigin");

            xml.startTag(null,"earthOrigin");
            xml.startTag(null, "x");
            xml.text(Level.earthOrigin.x+"");
            xml.endTag(null, "x");
            xml.startTag(null, "y");
            xml.text(Level.earthOrigin.y+"");
            xml.endTag(null, "y");
            xml.endTag(null, "earthOrigin");

            xml.startTag(null,"isCameraFollowsMissle");
            xml.text(Level.isCameraFollowsMissle+"");
            xml.endTag(null, "isCameraFollowsMissle");

            xml.startTag(null, "fileName");
            xml.text(Level.fileName);
            xml.endTag(null, "fileName");

            xml.startTag(null, "creatorId");
            xml.text(Level.creatorId);
            xml.endTag(null, "creatorId");

            xml.startTag(null, "uuid");
            xml.text(Level.uuid);
            xml.endTag(null, "uuid");

            xml.startTag(null, "isbuiltin");
            xml.text(Level.isBuiltIn+"");
            xml.endTag(null, "isbuiltin");


//			    ____     __      _____ __
//			   / __/__ _/ /____ / / (_) /____ ___
//			  _\ \/ _ `/ __/ -_) / / / __/ -_|_-<
//			 /___/\_,_/\__/\__/_/_/_/\__/\__/___/
//



            for (int i = 0; i < Level.satellites.size(); i++) {

                xml.startTag(null, "satellite");

                xml.startTag(null, "x");
                xml.text(Level.satellites.get(i).x+"");
                xml.endTag(null, "x");


                xml.startTag(null, "y");
                xml.text(Level.satellites.get(i).y+"");
                xml.endTag(null, "y");

                xml.startTag(null, "uuid");
                xml.text(Level.satellites.get(i).uniqueId);
                xml.endTag(null, "uuid");

                xml.endTag(null, "satellite");
            }


//			   _      __     ____
//			  | | /| / /__ _/ / /__
//			  | |/ |/ / _ `/ / (_-<
//			  |__/|__/\_,_/_/_/___/


            for (int i = 0; i < Level.walls.size(); i++) {

                xml.startTag(null, "wall");

                xml.startTag(null, "x");
                xml.text(Level.walls.get(i).x+"");
                xml.endTag(null, "x");


                xml.startTag(null, "y");
                xml.text(Level.walls.get(i).y+"");
                xml.endTag(null, "y");

                xml.startTag(null, "rotation");
                xml.text(Level.walls.get(i).rotation+"");
                xml.endTag(null, "rotation");

                xml.startTag(null, "width");
                xml.text(Level.walls.get(i).width+"");
                xml.endTag(null, "width");

                xml.startTag(null, "height");
                xml.text(Level.walls.get(i).height+"");
                xml.endTag(null, "height");

                xml.startTag(null, "uuid");
                xml.text(Level.walls.get(i).uniqueId);
                xml.endTag(null, "uuid");

                xml.endTag(null, "wall");
            }


//		     ______    _
//		    /_  __/___(_)__ ____ ____ _______
//		     / / / __/ / _ `/ _ `/ -_) __(_-<
//		    /_/ /_/ /_/\_, /\_, /\__/_/ /___/
//		              /___//___/

            for(int i = 0; i< Level.triggers.size(); i++){
                xml.startTag(null,  "trigger");

                xml.startTag(null, "x");
                xml.text(Level.triggers.get(i).x+"");
                xml.endTag(null, "x");

                xml.startTag(null, "y");
                xml.text(Level.triggers.get(i).y+"");
                xml.endTag(null, "y");

                xml.startTag(null,"width");
                xml.text(Level.triggers.get(i).width+"");
                xml.endTag(null, "width");

                xml.startTag(null, "height");
                xml.text(Level.triggers.get(i).height+"");
                xml.endTag(null, "height");

                xml.startTag(null,"rotation");
                xml.text(Level.triggers.get(i).rotation+"");
                xml.endTag(null, "rotation");

                if(Level.triggers.get(i).text!=null){
                    xml.startTag(null, "text");
                    xml.text(Level.triggers.get(i).text+"");
                    xml.endTag(null, "text");
                }

                if(Level.triggers.get(i).zoom!=null){
                    xml.startTag(null, "zoom");
                    xml.text(Level.triggers.get(i).zoom+"");
                    xml.endTag(null, "zoom");
                }



                xml.startTag(null, "uuid");
                xml.text(Level.triggers.get(i).uniqueId);
                xml.endTag(null, "uuid");


                xml.endTag(null, "trigger");

            }



            //POWERUPS


            for(int i = 0; i< Level.powerups.size(); i++){
                xml.startTag(null,  "powerup");

                xml.startTag(null, "x");
                xml.text(Level.powerups.get(i).x+"");
                xml.endTag(null, "x");

                xml.startTag(null, "y");
                xml.text(Level.powerups.get(i).y+"");
                xml.endTag(null, "y");


                if(Level.powerups.get(i).type!=null){
                    xml.startTag(null, "type");
                    xml.text(Level.powerups.get(i).type+"");
                    xml.endTag(null, "type");
                }

                if(Level.powerups.get(i).points!=null){
                    xml.startTag(null, "points");
                    xml.text(Level.powerups.get(i).points+"");
                    xml.endTag(null, "points");
                }



                xml.startTag(null, "uuid");
                xml.text(Level.powerups.get(i).uniqueId);
                xml.endTag(null, "uuid");


                xml.endTag(null, "powerup");

            }

            //WARPS

            for(int i = 0; i< Level.warps.size(); i++){
                xml.startTag(null,  "warp");

                xml.startTag(null, "x");
                xml.text(Level.warps.get(i).x+"");
                xml.endTag(null, "x");

                xml.startTag(null, "y");
                xml.text(Level.warps.get(i).y+"");
                xml.endTag(null, "y");


                xml.startTag(null, "rotation");
                xml.text(Level.warps.get(i).rotation+"");
                xml.endTag(null, "rotation");


                if(Level.warps.get(i).name!=null){
                    xml.startTag(null, "name");
                    xml.text(Level.warps.get(i).name);
                    xml.endTag(null, "name");
                }

                if(Level.warps.get(i).connectsTo!=null){
                    xml.startTag(null, "connectsTo");
                    xml.text(Level.warps.get(i).connectsTo+"");
                    xml.endTag(null, "connectsTo");
                }





                xml.startTag(null, "uuid");
                xml.text(Level.warps.get(i).uniqueId);
                xml.endTag(null, "uuid");


                xml.endTag(null, "warp");

            }


//			    ___       __               _    __
//			   / _ | ___ / /____ _______  (_)__/ /__
//			  / __ |(_-</ __/ -_) __/ _ \/ / _  (_-<
//			 /_/ |_/___/\__/\__/_/  \___/_/\_,_/___/


            for (int i = 0; i < Level.asteroids.size(); i++) {

                xml.startTag(null, "asteroid");

                xml.startTag(null, "x");
                xml.text(Level.asteroids.get(i).x+"");
                xml.endTag(null, "x");


                xml.startTag(null, "y");
                xml.text(Level.asteroids.get(i).y+"");
                xml.endTag(null, "y");

                xml.startTag(null, "releaseTime");
                xml.text(Level.asteroids.get(i).releaseTime+"");
                xml.endTag(null, "releaseTime");

                xml.startTag(null, "uuid");
                xml.text(Level.asteroids.get(i).uniqueId);
                xml.endTag(null, "uuid");

                xml.endTag(null, "asteroid");
            }

            if(Globals.instance().isDebug()){
                Log.w("Level.save",xml.toString());
            }

            xml.endDocument();

            xml.flush();

            //fos.flush();

            //fos.close();
            stream.flush();
            stream.close();


        } catch (Exception e) {
            Log.e("Level.save", e.toString());
        }


    }

	/* Pull down the entire XML mazes from the DB.
	 * Parse them into individual files on the disk.
	 * It grabs the fileName by parsing the XML. Each
	 * maze is on a separate line.
	 */

    public static void refreshOortCloud(final MyGameActivity context, final boolean isBuiltIn, final Callable callable){

        final String url = isBuiltIn? Globals.instance().getGetUrl(): Globals.instance().getUserGetUrl();

        Log.d("refreshOortCloud", "called with parameter: isBuiltIn="+isBuiltIn);


        new Thread(new Runnable() {

            @Override
            public void run() {

			/* in debug mode, a fresh pull is made from the server.
			 * but realistically, the first time a production user
			 * opens the game, the levels better be there and read to go.
			 */
                if(Globals.instance().isDebug()){
                    HttpClient client = new DefaultHttpClient();
                    HttpGet post = new HttpGet(url);

					/* We're going to refresh the oortcloud mazes only */
                    BasicHttpParams params = new BasicHttpParams();
                    params.setParameter("isbuiltin", "0");

                    try {
                        post.setParams(params);
                        HttpResponse response = client.execute(post);
                        BufferedReader in = new BufferedReader(new InputStreamReader(response.getEntity().getContent()));
                        String line = "";


                        StringBuilder sb = new StringBuilder();

                        while ((line = in.readLine()) != null) {

                            sb.append(line);
                        }
                        in.close();

                        if(isBuiltIn)
                            context.myDB.truncateLevels();
                        else
                            context.myDB.truncateUserLevels();

                        SAXLevelParser sax = new SAXLevelParser(new ByteArrayInputStream(sb.toString().getBytes("UTF-8"))){
                            public void onLevelParsed(){

                                LevelUtils.loadLevelIntoMemory(sb.toString(), context);
                                if(isBuiltIn)
                                    context.myDB.insertLevel();
                                else
                                    context.myDB.insertUserLevel();

                            }
                        };
                        sax.parse();

                    }
                    catch (UnsupportedEncodingException e) {
                        Log.e("LevelUtils.refreshOortCloud",e.toString());
                    }
                    catch(ClientProtocolException f){
                        Log.e("LevelUtils.refreshOortCloud",f.toString());
                    }
                    catch(IOException g){
                        Log.e("LevelUtils.refreshOortCloud",g.toString());
                    }

                }
                else {
					/* User is production user. Use the level string which is precompiled and released
					 * and not a "Live" version of the levels.
					 */

                    if(isBuiltIn)
                        context.myDB.truncateLevels();
                    else
                        context.myDB.truncateUserLevels();




                    StringBuilder sb = new StringBuilder();


                    if(isBuiltIn) {
                        sb.append(context.getString(R.string.levelsString));

                        try{

                            SAXLevelParser sax = new SAXLevelParser(new ByteArrayInputStream(sb.toString().getBytes("UTF-8"))){
                                public void onLevelParsed(){

                                    LevelUtils.loadLevelIntoMemory(sb.toString(), context);
                                    context.myDB.insertLevel();
                                }
                            };
                            sax.parse();

                        }
                        catch (UnsupportedEncodingException e) {
                            Log.e("LevelUtils.refreshOortCloud",e.toString());
                        }

                    }
                    else {

                        HttpClient client = new DefaultHttpClient();
                        HttpGet post = new HttpGet(url);

							/* We're going to refresh the user mazes only */
                        BasicHttpParams params = new BasicHttpParams();
                        params.setParameter("isbuiltin", "0");

                        try {
                            post.setParams(params);
                            HttpResponse response = client.execute(post);
                            BufferedReader in = new BufferedReader(new InputStreamReader(response.getEntity().getContent()));
                            String line = "";

                            while ((line = in.readLine()) != null) {

                                sb.append(line);
                            }
                            in.close();

                            SAXLevelParser sax = new SAXLevelParser(new ByteArrayInputStream(sb.toString().getBytes("UTF-8"))){
                                public void onLevelParsed(){

                                    LevelUtils.loadLevelIntoMemory(sb.toString(), context);
                                    context.myDB.insertUserLevel();

                                }
                            };
                            sax.parse();

                        }
                        catch (UnsupportedEncodingException e) {
                            Log.e("LevelUtils.refreshOortCloud",e.toString());
                        }
                        catch(ClientProtocolException f){
                            Log.e("LevelUtils.refreshOortCloud",f.toString());
                        }
                        catch(IOException g){
                            Log.e("LevelUtils.refreshOortCloud",g.toString());
                        }

                    }

                }

                if(callable!=null){
                    callable.call();
                }
            }


        }).start();

    }

	/*
	 * This voting system only allows you to vote once. Even if you change
	 * your mind! once you vote the buttons disappear and thats that. Returns
	 * true if voting is a success. False if the user already voted.
	 */

    public static boolean vote(final boolean upvote, final String reason, final Sound snd){


        final Set<String> votes = getVotes();
        final boolean alreadyVoted = votes.contains(Level.uuid);

        if(alreadyVoted){

            return !alreadyVoted;
        }

        new Thread(new Runnable() {


            @Override
            public void run() {


                if(upvote==true){
                    LevelUtils.statsUpvote();
                }
                else {
                    LevelUtils.statsDownvote();
                }

                votes.add(Level.uuid);
                setVotes(votes);

                HttpClient client = new DefaultHttpClient();
                HttpPost post = new HttpPost(Globals.instance().getVoteUrl());

                List<NameValuePair> params = new ArrayList<NameValuePair>();

                params.add(new BasicNameValuePair("userid", LevelUtils.getCreatorId()));
                params.add(new BasicNameValuePair("leveluuid", Level.uuid));
                params.add(new BasicNameValuePair("upvote", upvote==true? "1":"0"));
                if(reason!=null){
                    params.add(new BasicNameValuePair("reason",reason));
                }

                try {
                    post.setEntity(new UrlEncodedFormEntity(params));
                    final HttpResponse response = client.execute(post);

                    Log.w("LevelUtils.vote","userid="+LevelUtils.getCreatorId());
                    Log.w("LevelUtils.vote","leveluuid="+Level.uuid);
                    Log.w("LevelUtils.vote","upvote="+upvote);
                    Log.w("LevelUtils.vote","reason="+reason);

                    snd.setVolume(0.5f);
                    snd.play();

                }
                catch (UnsupportedEncodingException e) {
                    e.printStackTrace();
                }
                catch(ClientProtocolException f){
                    f.printStackTrace();
                }
                catch(IOException g){
                    g.printStackTrace();
                }


            }
        }).start();


        return !alreadyVoted;
    }


    /*
     * Returns the response from the server, if there was an
     * error or just otherwise just a status message.
     */
    public static String saveToOortCloud(final String xml, boolean isBuiltIn){

        String url = Globals.instance().getUserPostUrl();

        if(isBuiltIn){
            url = Globals.instance().getPostUrl();
        }

        Log.i("saveToOortCloud", "URL="+url);

        final StringBuilder responseText = new StringBuilder();


        //new Thread(new Runnable() {

        //@Override
        //public void run() {


        HttpClient client = new DefaultHttpClient();
        HttpPost post = new HttpPost(url);

        List<NameValuePair> params = new ArrayList<NameValuePair>();

        params.add(new BasicNameValuePair("uuid", Level.uuid));
        params.add(new BasicNameValuePair("filename", Level.fileName));
        params.add(new BasicNameValuePair("xml", xml));
        params.add(new BasicNameValuePair("isbuiltin", (Level.isBuiltIn==true?1:0)+""));
        params.add(new BasicNameValuePair("creatorId", Level.creatorId));

        try {
            post.setEntity(new UrlEncodedFormEntity(params));
            HttpResponse response = client.execute(post);

            BufferedReader rd = new BufferedReader(new InputStreamReader(response.getEntity().getContent()));
            String line = "";
            while ((line = rd.readLine()) != null) {
                responseText.append(line);
            }


            if(Globals.instance().isDebug()){
                Log.w("LevelUtils.saveToOortCloud","uuid="+Level.uuid);
                Log.w("LevelUtils.saveToOortCloud","filename="+Level.fileName);
                Log.w("LevelUtils.saveToOortCloud","xml="+xml);
                Log.w("LevelUtils.saveToOortCloud","isbuiltin="+(Level.isBuiltIn==true?1:0)+"");
                Log.w("LevelUtils.saveToOortCloud","creatorId="+Level.creatorId);
                Log.w("LevelUtils.saveToOortCloud","response code="+response.getStatusLine().getStatusCode());
            }


        }
        catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        catch(ClientProtocolException f){
            f.printStackTrace();
        }
        catch(IOException g){
            g.printStackTrace();
        }


        //}
        //}).start();


        return responseText.toString();
    }

    public static String requestFileName(){

        String url = Globals.instance().getFileNameUrl();

        final StringBuilder responseText = new StringBuilder();


        //new Thread(new Runnable() {

        //@Override
        //public void run() {


        HttpClient client = new DefaultHttpClient();
        HttpPost post = new HttpPost(url);

        List<NameValuePair> params = new ArrayList<NameValuePair>();


        try {
            post.setEntity(new UrlEncodedFormEntity(params));
            HttpResponse response = client.execute(post);

            BufferedReader rd = new BufferedReader(new InputStreamReader(response.getEntity().getContent()));
            String line = "";
            while ((line = rd.readLine()) != null) {
                responseText.append(line);
            }


            if(Globals.instance().isDebug()){
                Log.w("LevelUtils.getFileName", responseText.toString());
            }


        }
        catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        catch(ClientProtocolException f){
            f.printStackTrace();
        }
        catch(IOException g){
            g.printStackTrace();
        }


        //}
        //}).start();


        return responseText.toString();
    }



    public static String getCreatorId(){
        String radioId = android.os.Build.RADIO;
        String buildId = android.os.Build.SERIAL;
        String androId = Settings.Secure.ANDROID_ID;
        if(radioId!=null && !radioId.equalsIgnoreCase("unknown"))
            return radioId;
        else if(buildId!=null && !buildId.equalsIgnoreCase("unknown"))
            return buildId;
        else if(androId!=null && !androId.equalsIgnoreCase("unknown"))
            return androId;
        else
            return "";
    }

    public enum BadgeLocation{
        TOP_LEFT, TOP, TOP_RIGHT, BOTTOM_LEFT, BOTTOM, BOTTOM_RIGHT
    }

	/*
	 * Slap an icon in the bottom right corner of
	 * the sprite.
	 */

    public static void badge(IEntity sprite, IEntity sIcon, BadgeLocation location){
        float x = 0.0f;
        float y = 0.0f;

        float rot = sprite.getRotation();

        sprite.setRotation(0.0f);

        switch(location){
            case TOP_LEFT:
                x = 0.0f;
                y = sprite.getHeightScaled();
                break;
            case TOP:
                x = sprite.getWidthScaled()/2;
                y = sprite.getHeightScaled();
                break;
            case TOP_RIGHT:
                x = sprite.getWidthScaled();
                y = sprite.getHeightScaled();
                break;
            case BOTTOM_LEFT:
                x = 0.0f;
                y = 0.0f;
                break;
            case BOTTOM:
                x = sprite.getWidthScaled()/2;
                y = 0.0f;
                break;
            case  BOTTOM_RIGHT:
                x = sprite.getWidthScaled();
                y = 0.0f;
                break;
            default:
                break;
        }



        //xcos0 -ysin0, xsin0+ycos0
        if(rot!=0.0f){
            sIcon.setPosition(-((float)(x*Math.cos(-rot*Math.PI/180)) - (float)(y*Math.sin(-rot*Math.PI/180))),
                    ((float)(x*Math.sin(-rot*Math.PI/180)) + (float)(y*Math.cos(-rot*Math.PI/180))));

            Log.w("LevelUtils.badge", "rot="+rot);
            Log.w("LevelUtils.badge", "new position.x="+sIcon.getX()+" .y="+sIcon.getY());
            sIcon.setRotation(-rot);
        }
        else {
            sIcon.setPosition(x,y);
        }

        sprite.attachChild(sIcon);
        sprite.setRotation(rot);

    }


}

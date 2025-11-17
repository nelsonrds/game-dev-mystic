<?xml version="1.0" encoding="UTF-8"?>
<tileset version="1.10" tiledversion="1.11.2" name="tileset" tilewidth="16" tileheight="16" spacing="16" margin="8" tilecount="192" columns="12">
 <image source="tileset.png" width="384" height="512"/>
 <tile id="0">
  <objectgroup draworder="index" id="2">
   <object id="9" x="8.1875" y="9.0625" width="7.8125" height="6.9375"/>
  </objectgroup>
 </tile>
 <tile id="1">
  <objectgroup draworder="index" id="2">
   <object id="1" x="0" y="4.8125" width="16" height="10"/>
  </objectgroup>
 </tile>
 <tile id="8">
  <animation>
   <frame tileid="8" duration="200"/>
   <frame tileid="9" duration="200"/>
   <frame tileid="10" duration="200"/>
   <frame tileid="11" duration="200"/>
  </animation>
 </tile>
 <tile id="12">
  <objectgroup draworder="index" id="2">
   <object id="1" x="5.9375" y="0" width="6.0625" height="16"/>
  </objectgroup>
 </tile>
 <tile id="15">
  <objectgroup draworder="index" id="2">
   <object id="3" x="6.125" y="0" width="5.875" height="16"/>
  </objectgroup>
 </tile>
 <wangsets>
  <wangset name="Unnamed Set" type="corner" tile="-1">
   <wangcolor name="grass" color="#ff0000" tile="-1" probability="1"/>
   <wangcolor name="clif" color="#00ff00" tile="-1" probability="1"/>
   <wangtile tileid="0" wangid="0,1,0,2,0,1,0,1"/>
   <wangtile tileid="1" wangid="0,1,0,2,0,2,0,1"/>
   <wangtile tileid="2" wangid="0,1,0,1,0,2,0,1"/>
   <wangtile tileid="12" wangid="0,2,0,2,0,1,0,1"/>
   <wangtile tileid="13" wangid="0,2,0,2,0,2,0,2"/>
   <wangtile tileid="14" wangid="0,1,0,1,0,2,0,2"/>
   <wangtile tileid="24" wangid="0,2,0,1,0,1,0,1"/>
   <wangtile tileid="25" wangid="0,2,0,1,0,1,0,2"/>
   <wangtile tileid="26" wangid="0,1,0,1,0,1,0,2"/>
   <wangtile tileid="36" wangid="0,2,0,2,0,2,0,1"/>
   <wangtile tileid="37" wangid="0,1,0,2,0,2,0,2"/>
   <wangtile tileid="48" wangid="0,2,0,2,0,1,0,2"/>
   <wangtile tileid="49" wangid="0,2,0,1,0,2,0,2"/>
   <wangtile tileid="181" wangid="0,1,0,1,0,1,0,1"/>
  </wangset>
 </wangsets>
</tileset>

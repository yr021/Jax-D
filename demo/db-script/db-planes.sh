#!/bin/bash
 
  USER="db_user"
  PASSWORD="password_user"
  DATABASE="aero"
  TABLE="aircraft"
  Auth_TABLE="users"
  
  tput setaf 2
  echo 
  echo -e " ##################################### "
  echo -e " ######### Create DataBase ########### "
  echo -e " ------------------------------------- "
  echo -e " \e[90m$0        \e[32m                "
  echo 
  echo -e " ##  USER       : $USER                "
  echo -e " ##  PASSWORD   : $PASSWORD            "
  echo -e " ##  DATABASE   : $DATABASE            "
  echo -e " ##  Data_TABLE : $TABLE               "
  echo -e " ##  Auth_TABLE : $Auth_TABLE          "
  echo
  echo -e " ##################################### "
  echo 
  sleep 2
  tput setaf 7

  if which psql > /dev/null ; then
     echo " postgres command found ..   "  
  else     
     echo " postgres command not found  "
     echo " Script will abort           "
     exit 
  fi
    
  sudo -u postgres psql  2> /dev/null << EOF
  
  DROP  DATABASE $DATABASE ;
  DROP  USER     $USER     ;
 
  CREATE DATABASE $DATABASE TEMPLATE template0 ; 
  CREATE USER $USER WITH PASSWORD '$PASSWORD'  ;
  
  \connect $DATABASE ;  

  CREATE TABLE $TABLE (
	                model      	 varchar(255) ,
	                total_passengers integer      ,
	                distance_km	 integer      , 
	                speed_km_h       integer      ,
	                cost_euro     	 integer      ,
	
	                CONSTRAINT pk_aircraft PRIMARY KEY ( model )
  ) ;

  -- Source http://avions.findthebest.fr
  
  INSERT INTO aircraft VALUES ( 'Tupolev TU-414A'             , 26   , 15575 , 900  , 17  ) ;
  INSERT INTO aircraft VALUES ( 'Sukhoi SU-27UBK'             , 0    , 13401 , 1400 , 23  ) ;
  INSERT INTO aircraft VALUES ( 'Airbus ACJ319'               , 156  , 1112  , 828  , 37  ) ;
  INSERT INTO aircraft VALUES ( 'Gulfstream G650'             , 18   , 12964 , 904  , 44  ) ;
  INSERT INTO aircraft VALUES ( 'Bombardier Global 8000'      , 19   , 14631 , 904  , 58  ) ;
  INSERT INTO aircraft VALUES ( 'Boeing 787-9 Dreamliner'     , 2902 , 15742 , 945  , 140 ) ;
  INSERT INTO aircraft VALUES ( 'Boeing 777-200LR Worldliner' , 301  , 17400 , 945  , 171 ) ;
  INSERT INTO aircraft VALUES ( 'Airbus A340-500'             , 375  , 16668 , 907  , 203 ) ;
  INSERT INTO aircraft VALUES ( 'Boeing 747-400ER'            , 524  , 14205 , 1093 , 207 ) ;
  INSERT INTO aircraft VALUES ( 'Boeing 747-8'                , 700  , 14816 , 1043 , 212 ) ;

  GRANT SELECT ON $TABLE to $USER ;	
  
  -- Create Table for Authetication 
   
  CREATE TABLE $Auth_TABLE ( login     varchar(255) ,
                             password  varchar(255) ,
	
                            CONSTRAINT pk_users PRIMARY KEY ( login )
  
  ) ;

  INSERT INTO $Auth_TABLE VALUES ( 'admin'  , '21232f297a57a5a743894a0e4a801fc3' ) ; -- password is : MD5 ( admin )
  INSERT INTO $Auth_TABLE VALUES ( 'public' , '4c9184f37cff01bcdc32dc486ec36961' ) ; -- password is : MD5 ( public )
  
  GRANT SELECT ON $Auth_TABLE to $USER ;
  
  
EOF


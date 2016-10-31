CREATE KEYSPACE Metadata WITH placement_strategy = 'SimpleStrategy' AND strategy_options={replication_factor : 2};
CREATE KEYSPACE Tweets WITH placement_strategy = 'SimpleStrategy' AND strategy_options={replication_factor : 2};
CREATE KEYSPACE News WITH placement_strategy = 'SimpleStrategy' AND strategy_options={replication_factor : 2};

use Metadata;

create column family Filters
	with column_type = 'Super'
	and comparator = UTF8Type 
	and subcomparator = UTF8Type
	and default_validation_class = UTF8Type
	and key_validation_class = UTF8Type
	and comment='Structure: Filter[filtername][keyword][metadata_column]'
	AND column_metadata = [
		{column_name: start_date, validation_class: DateType}
		{column_name: end_date, validation_class: DateType}
		{column_name: is_monitored, validation_class: BooleanType}
	];
UPDATE COLUMN FAMILY Filters WITH compaction_strategy=LeveledCompactionStrategy AND compaction_strategy_options={sstable_size_in_mb: 10};
UPDATE COLUMN FAMILY Filters WITH rows_cached = 1.0;

create column family Filter_Keys 
   and comparator = UTF8Type 
   and key_validation_class = UTF8Type
   and column_metadata = [
      {column_name: key, validation_class: UTF8Type}
      {column_name: filtername, validation_class: UTF8Type,index_type: KEYS}
      {column_name: type, validation_class: UTF8Type, index_type: KEYS}
      {column_name: year, validation_class: IntegerType,index_type: KEYS}
      {column_name: month, validation_class: IntegerType,index_type: KEYS}
      {column_name: day, validation_class: IntegerType, index_type: KEYS}
      {column_name: timestamp, validation_class: DateType, index_type: KEYS}
      
   ];

%Sample entry from Filter_Keys
%RowKey: 1pc-20120702  // the row key, not directly retrievable.
%=> (column=day, value=2)  // day 1-31
%=> (column=filtername, value=1pc) // filtername
%=> (column=key, value=1pc-20120702) // full key
%=> (column=month, value=7)  // month 1-12
%=> (column=timestamp, value=2012-07-02 22:11:38-0700) // timestamp
%when this row was updated, mainly used for debugging.
%=> (column=type, value=tweets)  // the slice type [tweets, news]
%=> (column=year, value=2012) // year

   
create column family Filter_Metadata
 and comparator = UTF8Type 
   and key_validation_class = UTF8Type
   and column_metadata = [
      {column_name: name, validation_class: UTF8Type,index_type: KEYS} 
      {column_name: description, validation_class: UTF8Type}
      {column_name: user, validation_class: UTF8Type}
      {column_name: create_date, validation_class: DateType}
      {column_name: update_date, validation_class: DateType}
      ];
	  
	 




	 
create column family Filter_Stat
with column_type = 'Super'
	and comparator = DateType 
	and subcomparator = UTF8Type
	and default_validation_class = IntegerType
	and key_validation_class = UTF8Type
	and comment='Column family for Filter statisics. Add more features as necessary. Structure: Filter_statistics[keyword][date][feature]'
	and column_metadata = [		
		{column_name: tw_cnt, validation_class : IntegerType} 
		{column_name: usr_cnt, validation_class : IntegerType}
	];

	
% Filter Statistics Metadata
% Rowkey is keyword
create column family FilterStat_Metadata
 and comparator = UTF8Type 
   and key_validation_class = UTF8Type
   and column_metadata = [
      {column_name: filter, validation_class: UTF8Type}
      {column_name: start_date, validation_class: DateType}
      {column_name: end_date, validation_class: DateType}
      ];
	
use Tweets;

CREATE COLUMN FAMILY Tweet_Slice with column_type = 'Super'
	and comparator = LongType 
	and subcomparator = UTF8Type
	and default_validation_class = UTF8Type
	and key_validation_class = UTF8Type
	and comment = 'Structure: Filter[filter-dayslice][tweetId][metadata_column] where the super column key [filter-dayslice] is combination key with filtername and date in YYYYMMDD format'
	AND column_metadata = [
		{column_name: uid, validation_class: LongType}
		{column_name: cdt, validation_class: DateType}
		{column_name: rtid, validation_class: LongType}
		{column_name: ruid, validation_class: LongType}
		{column_name: rtwts, validation_class: LongType}
		{column_name: fav, validation_class: BooleanType}
		{column_name: txt, validation_class: UTF8Type}
		{column_name: src, validation_class: UTF8Type}
		{column_name: plc, validation_class: UTF8Type}
		{column_name: htgs, validation_class: UTF8Type}
		{column_name: ulnks, validation_class: UTF8Type}
		{column_name: mlnks, validation_class: UTF8Type}
		{column_name: ument, validation_class: UTF8Type}
		{column_name: geo, validation_class: UTF8Type}
		
];

UPDATE COLUMN FAMILY Tweet_Slice WITH compaction_strategy=LeveledCompactionStrategy AND compaction_strategy_options={sstable_size_in_mb: 20};

% Store all row keys in Tweet_Slice 
% Rowkey is still Filter-Date (e.g. ndq-20120701)
% With a range query to iterate all rows, then we can get all row keys in Tweet_Slice SCF
create column family TweetSlice_Rowkeys
 and comparator = UTF8Type 
   and key_validation_class = UTF8Type
   and column_metadata = [
      {column_name: filter, validation_class: UTF8Type}
      {column_name: date, validation_class: DateType}
      ];


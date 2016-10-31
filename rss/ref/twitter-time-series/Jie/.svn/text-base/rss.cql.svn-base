CREATE KEYSPACE Metadata WITH placement_strategy = 'SimpleStrategy' AND strategy_options={replication_factor : 2};
CREATE KEYSPACE Rss WITH placement_strategy = 'SimpleStrategy' AND strategy_options={replication_factor : 2};

use Metadata;

create column family RSS_Source_Category
with column_type = 'Super'
	and comparator = UTF8Type 
	and subcomparator = UTF8Type
	and default_validation_class = CounterColumnType
	and key_validation_class = UTF8Type
	and comment='Column family suggestion for Filter statisics. Add more features as necessary. Structure: Filter_statistics[filter][keyword][feature]'
	and column_metadata = [
		{column_name: id, validation_class: LongType}
		{column_name: father_id, validation_class: LongType}
		{column_name: name, validation_class: UTF8Type}
	];
   
create column family Feeds_Category
with column_type = 'Super'
	and comparator = UTF8Type 
	and subcomparator = UTF8Type
	and default_validation_class = CounterColumnType
	and key_validation_class = UTF8Type
	and comment='Column family suggestion for Filter statisics. Add more features as necessary. Structure: Filter_statistics[filter][keyword][feature]'
	and column_metadata = [
		{column_name: id, validation_class: LongType}
		{column_name: father_id, validation_class: LongType}
		{column_name: name, validation_class: UTF8Type}
	];
   
create column family Rss_Source
with column_type = 'Super'
	and comparator = UTF8Type 
	and subcomparator = UTF8Type
	and default_validation_class = CounterColumnType
	and key_validation_class = UTF8Type
	and comment='Column family suggestion for Filter statisics. Add more features as necessary. Structure: Filter_statistics[filter][keyword][feature]'
	and column_metadata = [
		{column_name: id, validation_class: LongType}
		{column_name: name, validation_class: UTF8Type}
		{column_name: url, validation_class: UTF8Type}
		{column_name: category, validation_class: LongType}
		{column_name: update_interval, validation_class: LongType}
	];
   
create column family Key_Words
with column_type = 'Super'
	and comparator = UTF8Type 
	and subcomparator = UTF8Type
	and default_validation_class = CounterColumnType
	and key_validation_class = UTF8Type
	and comment='Column family suggestion for Filter statisics. Add more features as necessary. Structure: Filter_statistics[filter][keyword][feature]'
	and column_metadata = [
		{column_name: id, validation_class: LongType}
		{column_name: name, validation_class: UTF8Type}
		{column_name: category, validation_class: LongType}
	];
   
use Rss;

CREATE COLUMN FAMILY RSS_Feeds with column_type = 'Super'
	and comparator = LongType 
	and subcomparator = UTF8Type
	and default_validation_class = UTF8Type
	and key_validation_class = UTF8Type
	and comment = 'Structure: Filter[filter-dayslice][tweetId][metadata_column] where the super column key [filter-dayslice] is combination key with filtername and date in YYYYMMDD format'
	AND column_metadata = [
		{column_name: id, validation_class: LongType}
		{column_name: time, validation_class: DateType}
		{column_name: category, validation_class: LongType}
		{column_name: rss_source, validation_class: LongType}
		{column_name: title, validation_class: UTF8Type}
		{column_name: brief, validation_class: UTF8Type}
		{column_name: url, validation_class: UTF8Type}
		{column_name: key_words, validation_class: UTF8Type}
		
];

UPDATE COLUMN FAMILY Tweet_Slice WITH compaction_strategy=LeveledCompactionStrategy AND compaction_strategy_options={sstable_size_in_mb: 20};

use News;

create column family Rss_Source
with column_type = 'Super'
	and comparator = UTF8Type 
	and subcomparator = UTF8Type
	and default_validation_class = CounterColumnType
	and key_validation_class = UTF8Type
	and comment='Column family suggestion for Filter statisics. Add more features as necessary. Structure: Filter_statistics[filter][keyword][feature]'
	and column_metadata = [
		{column_name: name, validation_class: UTF8Type}
		{column_name: url, validation_class: UTF8Type}
		{column_name: category, validation_class: UTF8Type}
		{column_name: update_interval, validation_class: LongType}
	];
   
use Rss;

CREATE COLUMN FAMILY NewsArticles with column_type = 'Super'
	and comparator = LongType 
	and subcomparator = UTF8Type
	and default_validation_class = UTF8Type
	and key_validation_class = UTF8Type
	and comment = 'Structure: Filter[filter-dayslice][newsId][metadata_column] where the super column key [filter-dayslice] is combination key with filtername and date in YYYYMMDD format'
	AND column_metadata = [
		{column_name: time, validation_class: DateType}
		{column_name: category, validation_class: LongType}
		{column_name: rss_source, validation_class: LongType}
		{column_name: title, validation_class: UTF8Type}
		{column_name: brief, validation_class: UTF8Type}
		{column_name: url, validation_class: UTF8Type}
		{column_name: count, validation_class: LongType}
		{column_name: related_rss, validation_class: LongType}
		
];
%rowkey: e.g., '1pc-20120614'
%newsID: automatic integer, generated by rss crawl program
%filter=null if we monitor all keywords, without filters
%count: how many articles are represented by this
%related_rss: other sources, separated by comma

CREATE COLUMN FAMILY NewsArticles_RowKeys
	and comparator = UTF8Type 
	and subcomparator = UTF8Type
	and default_validation_class = UTF8Type
	and key_validation_class = UTF8Type
	AND column_metadata = [
		{column_name: filter, validation_class: UTF8Type}
		{column_name: date, validation_class: DateType}		
];
%rowkey: e.g., '1pc-20120614'



UPDATE COLUMN FAMILY Tweet_Slice WITH compaction_strategy=LeveledCompactionStrategy AND compaction_strategy_options={sstable_size_in_mb: 20};

Ext.define('WindowSetApp.view.RemindTreeView' ,{
    extend: 'Ext.tree.Panel', 
    alias : 'widget.remindTreeView',
	border : false, // 没有边框
	autoWidth: true,
	height:Ext.getBody().getViewSize().height-110,
	frame: true,
	region: "center",
	store: "RemindTreeStore",
	layout:'fit',
	id:'remindTreeView',
    minWidth: 175,
    animCollapse: true,
    viewConfig : {   //checkbox联动
         onCheckboxChange : function(e, t) {
	          var item = e.getTarget(this.getItemSelector(), this.getTargetEl()), record;
	          if (item){
		           record = this.getRecord(item);
		           var check = !record.get('checked');
		           record.set('checked', check);
		           if (check) {
			            record.bubble(function(parentNode) {
				             parentNode.set('checked', true);
				             parentNode.expand(false, true);
			            });
			            record.cascadeBy(function(node) {
				             node.set('checked', true);
				             node.expand(false, true);
			            });
		           } else {
			            record.cascadeBy(function(node) {
			            	 node.set('checked', false);
			            });
		           }
	          }
	     }
    },
    rootVisible: false , //默认不显示根节点
    tbar:[{
        text:'打开选中提醒项',
        tooltip:'打开选中提醒项',
        iconCls:'common-assignauthority-icon',
        action : 'assignremind'
     }]
});


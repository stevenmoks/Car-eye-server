Ext.define('OperaTimeStatiApp.view.OperaTimeStatiSearchView' ,{
    extend: 'Ext.form.Panel',
    alias : 'widget.operaTimeStatiSearchView',
    itemId :'operaTimeStatiSearchView',
    title: '营运时长统计搜索',
    frame : true,
	region: "north",
	collapsible: true,
	collapseMode: "mini",
	split: true,
	bodyStyle : 'padding:4px 2px 3px 4px',
	layout : {
		type : 'table',
		align : 'right'
	},
    items : [
    		   {
								xtype : 'comboboxtree',
								fieldLabel : '企业名称',
								id : 'c_blocid',
								itemId:'blocid',
								width:150,
								labelWidth: 60,
								store: Ext.create('Ext.data.TreeStore', {  
							        autoLoad : 'true',
							        fields: ['text','id','parentId'], 
									root : {expanded : true,text : '企业名称' },
									proxy: {
										 type: 'ajax',
										 url: window.BIZCTX_PATH + '/servlet/DeptTree?type=200', 
										 reader: {
											 type: 'json'
										 }
									}
							    }) ,
							    rootVisible: false,
								editable: true,
								cls : 'x-textfield',
								valueField: 'id', 
								displayField: 'text',
								listeners: {
							        change: {
							            element: 'el', 
							            fn: function(){ 
							            	var store = Ext.getCmp('al_deptid').store;
											store.clearFilter(true);
											store.on('beforeload', function (store, options) {
									            var new_params = { 
									            	deptname: encodeURI(Ext.getCmp('al_deptid').getRawValue())
									            };
									            Ext.apply(store.proxy.extraParams, new_params);
									        });
									        store.reload(); 
							            }
							        }
								 }
								},{
									xtype : 'combo',
									width : 150,
									fieldLabel : '车牌号',
									labelWidth: 50,
									id:'c_carnumber',
									itemId:'carnumber',
									labelAlign: 'right',
									store : 'CarPageListStore',
									displayField : 'carnumber',
									valueField : 'carnumber',
									pageSize : 10,
									minChars:1,
									matchFieldWidth:false,
									listConfig :{
										width:235
									}
								},{
									xtype : 'datefield',
									width : 180,
									maxLength : 20,
									id : 'c_stime',
									fieldLabel : '开始日期',
									labelWidth: 60,
									format:'Y-m-d',
									 value: Ext.util.Format.date(new Date(), "Y-m-") + "01",
									labelAlign: 'right'
								},{
									xtype : 'datefield',
									width : 180,
									maxLength : 20,
									id : 'c_etime',
									fieldLabel : '结束日期',
									labelWidth: 60,
									format:'Y-m-d',
									value : new Date(),
									labelAlign: 'right'
								}
			],
	buttons : [{
				xtype: 'button',
				text : '查询',
				tooltip : '查询',
				iconCls : 'common-search-icon',
				action: 'search'
			},{
			    text : '重置',
			    tooltip : '清空查询条件',
			    iconCls : 'common-reset-icon',
		    	handler: function(button){
		        	button.up('form').getForm().reset();
		        }
			}]

});


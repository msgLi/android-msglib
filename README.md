# android-msglib

using:
    1.download source and ref lib
    2.create an application and extends MsgApplication
    3.in onCreate method,use MsgContext to init IOC or net or db or thread pools
    4.like:
      public BaseApplication extends MsgApplication{
        
          public void onCreate(){
            super.onCreate();
            IOCConfig iocConfig = new IOCConfig();
            ContextConfig contextConfig = new ContextConfig();
            contextConfig.setIocConfig(iocConfig);
            MsgContext.init(this,contextConfig);
          }
        
      }
      
项目旨在简化对UI的操作同时分离UI和业务层
  UI操作提供：
      BaseActivity
        initView()
        initData()
        initListener()
        
      BaseFragment
        initView()
        initData()
        initListener()
        
      @ContextUI
        用例分离UI、initView、initData、initListener
        
      @UISet
        将UI中的控件自动注入到对应的控件字段上
        
 业务层（IOC）
    @Service
      提供Service自动代理，但必须在resource/xml下编写ioc.xml,标签为
        <ioc>
          <bean id="" type=""></bean>
        </ioc>
        
        也可通过android studio插件IocConvert来自动生成
    @ViewControl
      视图控制层，可作为activity的逻辑组成部分，具体的视图逻辑由ViewControl来完成
      
      
      
      
      
      
      

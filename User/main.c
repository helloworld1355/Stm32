#include "stm32f10x.h"                  // Device header
#include "Delay.h"
#include "OLED.h"
#include "Motor.h"
#include "Key.h"
#include "AD.h"
#include "LightSensor.h"
#include "Serial.h"
#include "Timer.h"



uint8_t KeyNum;												//中断的引脚
uint8_t Speed;												//电机转速
uint8_t LI;														//光强
uint16_t ADValue;											//ADC的值
float KeySpeed;												//按钮控制增加的转速
uint8_t num;

static uint8_t low_LI = 50;					  //低于这个值就触发电机转动
static uint8_t high_LI = 80;					//高于这个值就不触发电机
static float maxSpeed=100.0;					//最大转速



int main(void)
{
	OLED_Init();												//OLED初始化
	Motor_Init();												//电机初始化
	Key_Init();													//按钮初始化--外部中断
	AD_Init();													//adc初始化
	LightSensor_Init();									//光敏传感器初始化
	Serial_Init();											//串口初始化
	Timer_Init();												//定时器初始化
	
	
	KeySpeed=0;													//初始化按钮控制转速
	Speed=0;														//初始化转速
	num=0;
	
	OLED_ShowString(1, 1, "Speed:");		
	OLED_ShowString(2, 1, "LY:");
	Serial_SendString("hello");
	while (1)
	{
		ADValue = AD_GetValue(ADC_Channel_1);						//获取adc的值--数模转换
		LI =100- (float)100 / 4095 * ADValue;						//转化为0-100数值，但是数据会丢失。
		KeyNum = Key_GetNum();													//获取外部中断事件
		
		/*  外部中断，1是B1脚，2是B11脚  */
		if (KeyNum == 1)
		{
			KeySpeed+=10;																	
			if(KeySpeed>maxSpeed) {
				Speed=maxSpeed;
				KeySpeed=maxSpeed;
			}			
		}else if(KeyNum == 2){
			KeySpeed-=10;
			if(KeySpeed<0) {
				Speed=0;
				KeySpeed=0;
			}	
		}
		
		/*  判断光照强度是否到达某一界限，low_LI、high_LI  */
		if(LI<low_LI){
			Speed=low_LI-LI+KeySpeed;
			if(Speed>maxSpeed) Speed=maxSpeed;
		}
		if(LI>low_LI) Speed=0+KeySpeed;
		if(LI>high_LI) Speed=0;
		
		/* 在OLED上显示信息 */		
		Motor_SetSpeed(Speed);
		OLED_ShowSignedNum(1, 7, Speed, 5);
		OLED_ShowNum(2, 4, LI, 3);
		OLED_ShowNum(3, 4, num, 3);
	}
}

/*  5秒中断一次  */
void TIM2_IRQHandler(void)
{
	if (TIM_GetITStatus(TIM2, TIM_IT_Update) == SET)
	{
		num++;
		Serial_Printf("LV:");
		Serial_SendNumber(LI,2);
		Serial_Printf("\t\tSpeed:");
		Serial_SendNumber(Speed,2);
		Serial_Printf("\r\n");
		TIM_ClearITPendingBit(TIM2, TIM_IT_Update);
	}
}


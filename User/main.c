#include "stm32f10x.h"                  // Device header
#include "Delay.h"
#include "OLED.h"
#include "Motor.h"
#include "Key.h"
#include "AD.h"
#include "LightSensor.h"
#include "Serial.h"
#include "Timer.h"
#include "W25Q64.h"
#include <stdlib.h>
#include <time.h>

uint8_t KeyNum;												//中断的引脚
uint8_t HsKeyNum;											//中断引脚
uint8_t Speed;												//电机转速
uint8_t LI;														//光强
uint16_t ADValue;											//ADC的值
uint8_t KeySpeed;											//按钮控制增加的转速
uint8_t *ArrayWrite;									//写入存储部件的数组
uint8_t ArrayRead[1];										//读存储部件
uint32_t NowAdress;										//最后一个写入数据的地址
uint32_t num;													//W25Q64数据的第几个

static uint8_t low_LI = 50;					  //低于这个值就触发电机转动
static uint8_t high_LI = 80;					//高于这个值就不触发电机
static float maxSpeed=100.0;					//最大转速
static uint32_t Flash_size = 0xffffff;//存储部件W25Q64最大存储容量

uint8_t MID;
uint16_t DID;

int main(void)
{
	OLED_Init();												//OLED初始化
	Motor_Init();												//电机初始化
	Key_Init();													//按钮初始化--外部中断
	AD_Init();													//adc初始化
	LightSensor_Init();									//光敏传感器初始化
	Serial_Init();											//串口初始化
	Timer_Init();												//定时器初始化
	W25Q64_Init();											//存储部件初始化
	
	KeySpeed=0;													//初始化按钮控制转速
	Speed=0;														//初始化转速
	num=0;															//存储数量重置
/*测试*/
	//ArrayRead = (uint8_t *)malloc(2 * sizeof(uint8_t));	//初始化读取数组，先读取存储头两位--存储部件0x000000前三位是扇区号，第四位是页号，后两位是页内地址。
	/*拿到最后一个写入数据的地址，并从之后开始写入*/
	/*
	uint8_t Arr[1]={2};
	W25Q64_PageProgram(0x000000,Arr,1);
	OLED_ShowNum(3,1,Arr[0],8);
	
		
	uint8_t Ree[1];
	W25Q64_ReadData(0x000000,Ree,1);
	OLED_ShowNum(4,3,Ree[0],8);
	*/
	
	
	
	//删除第一扇区的数据，最后可删除
	W25Q64_SectorErase(0x000000);
	
/*测试*/
	
/*读取所有的信息--可行*/
	NowAdress = W25Q64_searchAd(Flash_size,0,Flash_size);
//	ArrayRead = (uint8_t *)malloc((uint8_t)NowAdress * sizeof(uint8_t));
	W25Q64_ReadData(0x000000,ArrayRead,1);
	/*测试*/
	
	/*测试*/
	OLED_ShowString(1, 1, "Sp:");		
	OLED_ShowString(1, 10, "Lv:");
	OLED_ShowString(2 , 1 , "hs:");
	OLED_ShowString(3 , 1, "num:");
	OLED_ShowString(4 , 1, "Now:");
	
	Serial_SendString("hello");
	
	while (1) 
	{
		ADValue = AD_GetValue(ADC_Channel_1);						//获取adc的值--数模转换
		LI =100- (float)100 / 4095 * ADValue;						//转化为0-100数值，但是数据会丢失。
		KeyNum = Key_GetNum();			//获取外部中断事件
		HsKeyNum = Key_GetHsNum();
		
		/*  外部中断，1是B1脚，2是B11脚  */
		if (KeyNum == 1)
		{
			KeySpeed+=10;																	
			if(KeySpeed>maxSpeed) {
				Speed=maxSpeed;
				KeySpeed=maxSpeed;
			}			
		}else if(KeyNum == 2){
			
			if(KeySpeed<10) {
				Speed=0;
				KeySpeed=0;
			}else KeySpeed -= 10;
		} 
		if(HsKeyNum ==1){
			num = (num+NowAdress-1) % NowAdress;
			W25Q64_ReadData(num,ArrayRead,1);
		}else if( HsKeyNum ==2){
			num = (num+1) % NowAdress;
			W25Q64_ReadData(num,ArrayRead,1);
		}
		
		/*  判断光照强度是否到达某一界限，low_LI、high_LI  */
		if(LI<low_LI){
			Speed=20+low_LI-LI+KeySpeed;
			if(Speed>=maxSpeed) Speed=maxSpeed;
		}else if(LI>low_LI) Speed=0+KeySpeed;
		
		if(LI>high_LI) Speed=0;
		
		/* 在OLED上显示信息 */		
		Motor_SetSpeed(Speed);
		OLED_ShowSignedNum(1, 4, Speed, 3);
		OLED_ShowNum(1, 13, LI, 2);
		OLED_ShowNum(2,4,ArrayRead[0],3);
		OLED_ShowNum(3,5,num,8);
		OLED_ShowNum(4,5,NowAdress,8);
		
	}
}

/*  5秒中断一次  */
void TIM1_UP_IRQHandler(void)
{
	if (TIM_GetITStatus(TIM1, TIM_IT_Update) == SET)
	{
		//写入W25Q64 通过NowAdress写入	
		W25Q64_PageProgram(NowAdress,&LI,1);
		NowAdress+=1;
		
		
		//发送串口数据
		Serial_Printf("LV:");
		Serial_SendNumber(LI,2);
		Serial_Printf("\tSpeed:");
		Serial_SendNumber(Speed,3);
		Serial_Printf("\tnum:");
		Serial_SendNumber(NowAdress,3);
		Serial_Printf("\r\n");
		TIM_ClearITPendingBit(TIM1, TIM_IT_Update);
	}
}


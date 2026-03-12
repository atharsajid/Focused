import android.util.Log
import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.*

class TimerViewModel : ViewModel() {
    private var _totalTime = mutableStateOf(1500L) // 25 mins
    val totalTime: State<Long> = _totalTime

    private var _timeLeft = mutableStateOf(1500L)
    val timeLeft: State<Long> = _timeLeft

    private var _isRunning = mutableStateOf(false)
    val isRunning: State<Boolean> = _isRunning

    private var timerJob: Job? = null

    fun setTimer(minutes: Int) {
        Log.d("TimerDebug", "Setting timer to: $minutes minutes")
        stopTimer()
        val seconds = minutes * 60L
        _totalTime.value = seconds
        _timeLeft.value = seconds
    }

    fun startTimer() {
        if (_isRunning.value) return
        Log.d("TimerDebug", "Timer Started")
        _isRunning.value = true

        timerJob = viewModelScope.launch(Dispatchers.Main) {
            while (_timeLeft.value > 0 && _isRunning.value) {
                delay(1000L)
                _timeLeft.value -= 1
                Log.d("TimerDebug", "Time left: ${_timeLeft.value}")
            }
            if (_timeLeft.value <= 0) {
                _isRunning.value = false
                Log.d("TimerDebug", "Timer Finished")
            }
        }
    }

    fun stopTimer() {
        Log.d("TimerDebug", "Timer Stopped")
        _isRunning.value = false
        timerJob?.cancel()
    }

    fun formatTime(): String {
        val minutes = _timeLeft.value / 60
        val seconds = _timeLeft.value % 60
        return "%02d:%02d".format(minutes, seconds)
    }

    fun getProgress(): Float {
        return if (_totalTime.value > 0) {
            _timeLeft.value.toFloat() / _totalTime.value.toFloat()
        } else 0f
    }
}